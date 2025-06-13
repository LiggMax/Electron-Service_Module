package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.entity.OrderEntity;
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
import com.ligg.service.annotation.Bill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;


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
    @Bill(remark = "购买项目")
    public Map<String, Object> buyProject(Long userId, Integer regionId, Integer projectId, Integer quantity) {
        log.info("用户[{}]开始购买项目[{}]，地区[{}]，数量[{}]", userId, projectId, regionId, quantity);

        Map<String, Object> result = new HashMap<>();
        List<String> purchasedPhones = new ArrayList<>();

        // 获取用户和项目信息
        UserEntity userInfo = userMapper.selectById(userId);
        ProjectEntity projectEntity = projectMapper.selectById(projectId);

        if (userInfo == null) {
            result.put("error", "用户信息不存在");
            return result;
        }

        if (projectEntity == null) {
            result.put("error", "项目信息不存在");
            return result;
        }

        // 计算总费用
        Float unitPrice = projectEntity.getProjectPrice();
        Float totalPrice = unitPrice * quantity;

        // 检查用户余额
        if (userInfo.getMoney() < totalPrice) {
            result.put("error", "余额不足");
            return result;
        }

        // 获取可用号码列表
        List<PhoneEntity> phoneEntities = phoneNumberMapper.getAvailablePhonesByProject(regionId, projectId);

        if (phoneEntities.size() < quantity) {
            result.put("error", String.format("该地区可用号码不足，需要%d个，仅有%d个可用", quantity, phoneEntities.size()));
            return result;
        }

        // 随机打乱号码列表并取前quantity个
        Collections.shuffle(phoneEntities);
        List<PhoneEntity> selectedPhones = phoneEntities.subList(0, quantity);

        // 按phoneId排序，避免死锁
        selectedPhones.sort(Comparator.comparing(PhoneEntity::getPhoneId));

        List<ReentrantLock> acquiredLocks = new ArrayList<>();
        int successCount = 0;
        List<String> failedReasons = new ArrayList<>();

        try {
            // 批量获取锁
            for (PhoneEntity phone : selectedPhones) {
                String lockKey = phone.getPhoneId() + "_" + projectId;
                ReentrantLock phoneLock = phoneLocks.computeIfAbsent(lockKey, k -> new ReentrantLock());

                boolean lockAcquired;
                try {
                    lockAcquired = phoneLock.tryLock(2, TimeUnit.SECONDS);
                    if (!lockAcquired) {
                        failedReasons.add("号码" + phone.getPhoneNumber() + "获取锁超时");
                        break;
                    }
                    acquiredLocks.add(phoneLock);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    failedReasons.add("购买过程被中断");
                    break;
                }
            }

            // 如果没有获取到所有锁，则返回错误
            if (acquiredLocks.size() != selectedPhones.size()) {
                result.put("error", "部分号码正在被其他用户购买，请重试");
                return result;
            }

            // 双重检查所有号码是否仍然可用
            for (PhoneEntity phone : selectedPhones) {
                if (!isPhoneAvailableForProject(phone.getPhoneId(), projectId)) {
                    failedReasons.add("号码" + phone.getPhoneNumber() + "已被其他用户购买");
                }
            }

            if (!failedReasons.isEmpty()) {
                result.put("error", String.join(", ", failedReasons));
                return result;
            }

            // 批量处理购买
            for (PhoneEntity phone : selectedPhones) {
                try {
                    // 更新号码项目关联表状态
                    int updateResult = phoneProjectRelationMapper.updateAvailableStatusWithCondition(
                            phone.getPhoneId(), projectId);

                    if (updateResult == 0) {
                        failedReasons.add("号码" + phone.getPhoneNumber() + "状态更新失败");
                        continue;
                    }

                    // 添加用户订单
                    String ordersId = UUID.randomUUID().toString();
                    int addResult = userMapper.addPhoneNumber(ordersId, userId, phone.getPhoneNumber(),
                            phone.getAdminUserId(), projectId, unitPrice, regionId);

                    if (addResult > 0) {
                        OrderEntity order = new OrderEntity();
                        order.setOrdersId(ordersId);
                        order.setUserId(userId);
                        order.setProjectMoney(unitPrice);
                        order.setCreatedAt(LocalDateTime.now());
                        order.setPhoneNumber(phone.getPhoneNumber());

                        //  将号码信息保存到Redis中
                        redisTemplate.opsForValue().set("user:orders:" + userId + ":" + ordersId,
                                objectMapper.writeValueAsString(order), 20, TimeUnit.MINUTES);

                        purchasedPhones.add(String.valueOf(phone.getPhoneNumber()));
                        successCount++;
                        log.info("用户[{}]成功购买号码[{}]", userId, phone.getPhoneNumber());
                    } else {
                        // 回滚号码状态
                        phoneProjectRelationMapper.rollbackAvailableStatus(phone.getPhoneId(), projectId);
                        failedReasons.add("号码" + phone.getPhoneNumber() + "订单创建失败");
                    }
                } catch (Exception e) {
                    log.error("处理号码[{}]时发生错误: {}", phone.getPhoneNumber(), e.getMessage());
                    failedReasons.add("号码" + phone.getPhoneNumber() + "处理异常");
                }
            }

            // 如果有成功购买的号码，更新用户余额
            if (successCount > 0) {
                Float actualCost = unitPrice * successCount;
                userMapper.updateUserMoney(userId, actualCost);

                // 构建成功结果
                result.put("success", true);
                result.put("orderId", UUID.randomUUID().toString().replace("-", ""));
                result.put("purchasedPhones", purchasedPhones);
                result.put("successCount", successCount);
                result.put("totalCost", actualCost);
                result.put("unitPrice", unitPrice);

                log.info("用户[{}]购买完成，成功{}个，总费用{}元", userId, successCount, actualCost);

                // 如果部分失败，添加警告信息
                if (successCount < quantity) {
                    result.put("warning", String.format("仅成功购买%d个号码，失败原因: %s",
                            successCount, String.join(", ", failedReasons)));
                }
            } else {
                result.put("error", "所有号码购买失败: " + String.join(", ", failedReasons));
            }

        } catch (Exception e) {
            log.error("用户[{}]购买项目[{}]失败: {}", userId, projectId, e.getMessage(), e);
            result.put("error", "购买过程中发生系统错误，请稍后重试");
            throw e;
        } finally {
            // 释放所有获取的锁
            for (ReentrantLock lock : acquiredLocks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

            // 清理长时间未使用的锁
            cleanupUnusedLocks();
        }

        return result;
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