package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.UserEntity;
import com.ligg.mapper.CustomerMapper;
import com.ligg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, UserEntity> implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

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
    }
}
