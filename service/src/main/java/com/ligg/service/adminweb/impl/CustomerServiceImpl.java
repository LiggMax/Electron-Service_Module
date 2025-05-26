package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.mapper.CustomerMapper;
import com.ligg.service.adminweb.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, UserEntity> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 更新用户状态
     */
    @Override
    public void updateUserStatus(Long userId, Boolean status) {
        Integer userStatus = status ? 1 : 0;

        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getUserId, userId)
                .set(UserEntity::getUserStatus, userStatus);
        customerMapper.update(null, updateWrapper);
        //删除token
        redisTemplate.delete("Token:" + userId);
    }

    /**
     * 修改用户密码
     */
    @Override
    public void updatePassword(Long userId, String password) {
        String encrypt = BCryptUtil.encrypt(password);
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getUserId, userId)
                .set(UserEntity::getPassword, encrypt);
        customerMapper.update(updateWrapper);
    }

    @Override
    public void saveUser(UserEntity userEntity) {
        userEntity.setPassword(BCryptUtil.encrypt(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        customerMapper.insert(userEntity);
    }
}
