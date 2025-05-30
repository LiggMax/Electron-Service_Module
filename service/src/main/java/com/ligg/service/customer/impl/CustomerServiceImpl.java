package com.ligg.service.customer.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.*;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.entity.user.UserFavoriteEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.UserDataVo;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.PhoneProjectRelationMapper;
import com.ligg.mapper.ProjectMapper;
import com.ligg.mapper.user.UserMapper;
import com.ligg.service.customer.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements CustomerService {

    // 线程锁
    private final ReentrantLock buyProjectLock = new ReentrantLock();

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private PhoneProjectRelationMapper phoneProjectRelationMapper;

    /**
     * 根据账号查询管理员用户信息
     */
    @Override
    public MerchantEntity findByAdminUser(String account) {
        return userMapper.findByAdminUser(account);
    }

    /**
     * 生成token
     */
    @Override
    public String createToken(Long userId, String account) {

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("account", account);
        String token = jwtUtil.createToken(claims);
        redisTemplate.opsForValue()
                .set("Token:" + userId, token,
                        6, TimeUnit.HOURS);
        return token;
    }

    /**
     * 清除token
     */
    @Override
    public void clearToken(Long userId) {
        redisTemplate.delete("Token:" + userId);
    }

    /**
     * 根据id获取用户信息
     */
    @Override
    public MerchantEntity findByAdminUserInfo(Long userId) {
        return userMapper.findByAdminUserInfo(userId);
    }

    /**
     * 根据账号和密码查询用户信息
     */
    @Override
    public UserEntity findByUser(String account) {
        return userMapper.findByUser(account);
    }

    /**
     * 根据id查询用户信息
     */
    @Override
    public UserEntity findByUserInfo(Long userId) {
        return userMapper.selectById(userId);
    }

    /**
     * 更新用户信息
     */
    @Override
    public String updateUserInfo(UserDataVo userDataVo) {

        if (!Objects.equals(userDataVo.getNewPassword(), "")) {
            UserEntity byUserInfo = userMapper.findByUserInfo(userDataVo.getUserId());
            if (userDataVo.getOldPassword().equals(byUserInfo.getPassword())) {
                userMapper.updateUserPassword(userDataVo.getUserId(), userDataVo.getNewPassword());
            } else {
                return "您输入的原始密码不正确";
            }
        }
        userMapper.updateUserInfo(userDataVo);
        return null;
    }

    @Override
    public String addUserFavorite(UserFavoriteEntity userFavoriteEntity) {

        if (userMapper.getUserFavorite(userFavoriteEntity.getUserId(), userFavoriteEntity.getProjectId()) != null) {
            return "该项目已收藏";
        }
        userMapper.addUserFavorite(userFavoriteEntity);
        return null;
    }

    /**
     * 购买项目
     */
    @Override
    @Transactional
    public String buyProject(Long userId, Integer regionId, Integer projectId) {
        // 获取线程锁，确保同一时间只有一个购买请求被处理
        buyProjectLock.lock();

        try {
            log.info("用户[{}]开始购买项目[{}]，地区[{}]，获取到线程锁", userId, projectId, regionId);

            // 从数据库中获取指定区域和项目ID对应的号码列表(只返回100条数据)
            List<PhoneEntity> phoneEntities = phoneNumberMapper.getPhonesByProject(regionId);
            // 从phoneEntities列表中随机获取一个号码
            if (!phoneEntities.isEmpty()) {
                int randomIndex = (int) (Math.random() * phoneEntities.size());
                PhoneEntity phoneEntity = phoneEntities.get(randomIndex);
                log.info("用户[{}]从[{}]中随机获取了手机号[{}],卡商id[{}]", userId, regionId, phoneEntity.getPhoneNumber(), phoneEntity.getAdminUserId());
                //获取卡商id
                Long adminUserId = phoneEntity.getAdminUserId();
                //获取用户数据
                UserEntity userInfo = userMapper.selectById(userId);
                //获取项目价格
                ProjectEntity projectEntity = projectMapper.selectById(projectId);
                //TODO 暂时的号码价格
                Float phoneMoney = 0.20f;
                Float projectMoney = projectEntity.getProjectPrice();
                if (userInfo.getMoney() < projectMoney + phoneMoney) {
                    return "您的余额不足";
                }

                /*
                   使用事务来确保操作的原子性
                   添加号码订单
                 */
                int addResult = userMapper.addPhoneNumber(userId, phoneEntity.getPhoneNumber(), adminUserId, projectId, projectMoney, phoneMoney, regionId);
                if (addResult > 0) {
                    // 只有在购买号码成功时才更新号码项目关联表状态
                    phoneProjectRelationMapper.updateAvailableStatus(phoneEntity.getPhoneId(), projectId);

                    // 更新用户余额
                    userMapper.updateUserMoney(userId, projectEntity.getProjectPrice() + phoneMoney);
                }
                return null;
            }
            return "号码可能已经被购买";
        } catch (Exception e) {
            log.error("用户[{}]购买项目[{}]失败: {}", userId, projectId, e.getMessage(), e);
            throw e; // 重新抛出异常以触发事务回滚
        } finally {
            // 无论成功还是失败，都要释放锁
            buyProjectLock.unlock();
            log.info("用户[{}]购买项目[{}]处理完成，已释放线程锁", userId, projectId);
        }
    }


    /**
     * 获取用户收藏的项目
     */
    @Override
    public List<Map<String, Object>> getUserFavorite(Long userId) {
        return userMapper.getUserFavoriteByUserId(userId);
    }

    /**
     * 获取用户订单
     */
    @Override
    public List<Map<String, Object>> getUserOrder(Long userId) {
        return userMapper.getUserOrder(userId);
    }

    /**
     * 账号注销
     */
    @Override
    public void logoutAccount(Long userId) {
        userMapper.logoutAccount(userId, 0);
    }

    /**
     * 根据账号和密码查询用户信息
     */
    @Override
    public UserEntity findAccountAndPasswordByUser(String account) {
        return userMapper.findAccountAndPasswordByUser(account);
    }

    /**
     * 注册账号
     */
    @Override
    public void registerAccount(String account, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setAccount(account);
        userEntity.setPassword(BCryptUtil.encrypt(password));

        //生成20位数的UUID用户邀请码
        userEntity.setInvitationCode(UUID.randomUUID().toString().replace("-", "").substring(0, 10));
        userMapper.insert(userEntity);
    }

    /**
     * 获取管理信息
     */
    @Override
    public AdminWebUserEntity getAdminWebInfo(String account) {
        return userMapper.findByAdminWebUser(account);
    }

    /**
     * 更新登录时间
     */
    @Override
    public void updateLoginTime(Long userId) {
        LambdaUpdateWrapper<UserEntity> wrapper = new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getUserId, userId)
                .set(UserEntity::getLoginTime, LocalDateTime.now());
        userMapper.update(null, wrapper);
    }
}