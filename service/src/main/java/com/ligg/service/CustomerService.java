package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.UserEntity;

public interface CustomerService extends IService<UserEntity> {
    void updateUserStatus(Long userId, Boolean status);
}
