package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.user.UserEntity;

public interface CustomerManageService extends IService<UserEntity> {
    //用户状态
    void updateUserStatus(Long userId, Boolean status);
    // 重置密码
    void updatePassword(Long userId, String password);
    //添加客户
    void saveUser(UserEntity userEntity);

    //修改客户信息
    void updateCustomerInfoById(UserEntity userEntity);

    //修改客户余额
    void updateBalance(Long userId, Float balance, Boolean isType);
}
