package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.UserEntity;

public interface CustomerService extends IService<UserEntity> {
    //用户状态
    void updateUserStatus(Long userId, Boolean status);
    // 重置密码
    void updatePassword(Long userId, String password);
}
