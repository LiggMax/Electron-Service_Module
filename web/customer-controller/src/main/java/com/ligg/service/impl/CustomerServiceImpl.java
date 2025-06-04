package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.entity.user.UserFavoriteEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.vo.UserDataVo;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.PhoneProjectRelationMapper;
import com.ligg.mapper.ProjectMapper;
import com.ligg.mapper.user.UserMapper;
import com.ligg.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class CustomerServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements CustomerService {

    // 基于号码+项目的细粒度锁映射
    private final ConcurrentHashMap<String, ReentrantLock> phoneLocks = new ConcurrentHashMap<>();

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private ProjectMapper projectMapper;

    @Autowired
    private PhoneProjectRelationMapper phoneProjectRelationMapper;

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
        log.info("用户[{}]开始购买项目[{}]，地区[{}]", userId, projectId, regionId);

        // 获取可用号码列表，增加可用状态过滤
        List<PhoneEntity> phoneEntities = phoneNumberMapper.getAvailablePhonesByProject(regionId, projectId);

        if (phoneEntities.isEmpty()) {
            return "该地区暂无可用号码";
        }

        // 随机选择一个号码
        int randomIndex = (int) (Math.random() * phoneEntities.size());
        PhoneEntity phoneEntity = phoneEntities.get(randomIndex);

        // 构建锁的key：phoneId + projectId
        String lockKey = phoneEntity.getPhoneId() + "_" + projectId;

        // 获取或创建对应的锁
        ReentrantLock phoneLock = phoneLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());

        // 尝试获取锁，最多等待3秒
        boolean lockAcquired = false;
        try {
            lockAcquired = phoneLock.tryLock(3, TimeUnit.SECONDS);
            if (!lockAcquired) {
                log.warn("用户[{}]购买号码[{}]项目[{}]获取锁超时", userId, phoneEntity.getPhoneNumber(), projectId);
                return "服务繁忙，请稍后重试";
            }

            log.info("用户[{}]获取到号码[{}]项目[{}]的锁", userId, phoneEntity.getPhoneNumber(), projectId);

            // 再次检查号码是否可用（双重检查）
            if (!isPhoneAvailableForProject(phoneEntity.getPhoneId(), projectId)) {
                log.warn("号码[{}]项目[{}]已被其他用户购买", phoneEntity.getPhoneNumber(), projectId);
                return "号码已被其他用户购买，请重新选择";
            }

            // 获取用户和项目信息
            UserEntity userInfo = userMapper.selectById(userId);
            ProjectEntity projectEntity = projectMapper.selectById(projectId);

            // 获取费用
            Float projectMoney = projectEntity.getProjectPrice();

            if (userInfo.getMoney() < projectMoney) {
                return "您的余额不足";
            }

            // 尝试更新号码项目关联表状态（原子操作）
            int updateResult = phoneProjectRelationMapper.updateAvailableStatusWithCondition(
                    phoneEntity.getPhoneId(), projectId);

            if (updateResult == 0) {
                log.warn("号码[{}]项目[{}]状态更新失败，可能已被其他用户购买",
                        phoneEntity.getPhoneNumber(), projectId);
                return "号码已被其他用户购买，请重新选择";
            }

            // 添加用户订单
            int addResult = userMapper.addPhoneNumber(userId, phoneEntity.getPhoneNumber(),
                    phoneEntity.getAdminUserId(), projectId, projectMoney, regionId);

            if (addResult > 0) {
                // 更新用户余额
                userMapper.updateUserMoney(userId, projectMoney);

                log.info("用户[{}]成功购买项目[{}]的手机号[{}]，花费{}元",
                        userId, projectId, phoneEntity.getPhoneNumber(), projectMoney);
                return null;
            } else {
                // 如果订单创建失败，需要回滚号码状态
                phoneProjectRelationMapper.rollbackAvailableStatus(phoneEntity.getPhoneId(), projectId);
                return "订单创建失败，请重试";
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("用户[{}]购买项目[{}]被中断", userId, projectId);
            return "服务中断，请重试";
        } catch (Exception e) {
            log.error("用户[{}]购买项目[{}]失败: {}", userId, projectId, e.getMessage(), e);
            throw e;
        } finally {
            if (lockAcquired) {
                phoneLock.unlock();
                log.info("用户[{}]释放号码[{}]项目[{}]的锁", userId, phoneEntity.getPhoneNumber(), projectId);

                // 清理长时间未使用的锁
                cleanupUnusedLocks();
            }
        }
    }

    /**
     * 检查号码是否对指定项目可用
     */
    private boolean isPhoneAvailableForProject(Long phoneId, Integer projectId) {
        return phoneProjectRelationMapper.checkPhoneProjectAvailable(phoneId, projectId) > 0;
    }

    /**
     * 清理长时间未使用的锁（防止内存泄漏）
     */
    private void cleanupUnusedLocks() {
        // 简单的清理策略：当锁映射大小超过1000时进行清理
        if (phoneLocks.size() > 1000) {
            Iterator<Map.Entry<String, ReentrantLock>> iterator = phoneLocks.entrySet().iterator();
            int cleanedCount = 0;
            while (iterator.hasNext() && cleanedCount < 100) {
                Map.Entry<String, ReentrantLock> entry = iterator.next();
                ReentrantLock lock = entry.getValue();

                // 如果锁没有被持有且没有等待线程，则移除
                if (!lock.isLocked() && !lock.hasQueuedThreads()) {
                    iterator.remove();
                    cleanedCount++;
                }
            }
            log.info("清理了{}个未使用的锁", cleanedCount);
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