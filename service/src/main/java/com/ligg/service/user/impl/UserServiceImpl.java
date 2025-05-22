package com.ligg.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.*;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.UserDataVo;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.ProjectMapper;
import com.ligg.mapper.user.UserMapper;
import com.ligg.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {


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
    /**
     * 根据账号查询管理员用户信息
     */
    @Override
    public AdminUserEntity findByAdminUser(String account) {
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
    public AdminUserEntity findByAdminUserInfo(Long userId) {

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
        List<PhoneEntity> phoneEntities = phoneNumberMapper.getPhonesByProject(regionId);
        // 从phoneEntities列表中随机获取一个号码
        if (!phoneEntities.isEmpty()) {
            int randomIndex = (int) (Math.random() * phoneEntities.size());
            PhoneEntity phoneEntity = phoneEntities.get(randomIndex);
            log.info("用户[{}]从[{}]中随机获取了手机号[{}],卡商id[{}]", userId, regionId, phoneEntity.getPhoneNumber(),phoneEntity.getAdminUserId());
            //获取卡商id
            Long adminUserId = phoneEntity.getAdminUserId();
            //获取用户id
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
            int addResult = userMapper.addPhoneNumber(userId, phoneEntity.getPhoneNumber(), adminUserId,projectId, projectMoney , phoneMoney);
            if (addResult > 0) {
                // 只有在购买号码成功时才更新号码状态
                phoneNumberMapper.update(new LambdaUpdateWrapper<PhoneEntity>()
                        .eq(PhoneEntity::getPhoneNumber, phoneEntity.getPhoneNumber())
                        .set(PhoneEntity::getUsageStatus, 0));

                // 更新用户余额
                userMapper.updateUserMoney(userId,projectEntity.getProjectPrice() + phoneMoney);

                // 删除phone_project_relation表中的关联数据
                phoneNumberMapper.deletePhoneProjectRelation(phoneEntity.getPhoneId(), projectId);
                
                // 记录日志
                log.info("用户[{}]成功购买项目[{}]的手机号[{}]，并从关联表中移除", 
                        userId, projectId, phoneEntity.getPhoneNumber());
            }
            return null;
        }
        return "号码可能已经被购买";
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
        userMapper.logoutAccount(userId,0);
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
        userEntity.setInvitationCode(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
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
