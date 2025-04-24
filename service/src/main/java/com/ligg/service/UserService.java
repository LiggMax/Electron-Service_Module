package com.ligg.service;


import com.ligg.common.entity.AdminUserEntity;

public interface UserService {

    //根据账号和密码查询用户
    AdminUserEntity findByAdminUser(String account, String password);

    //生成token
    String createToken(AdminUserEntity user);

    //清理token
    void clearToken(String userId);

    //根据用户id查询用户信息
    AdminUserEntity findByAdminUserInfo(String userId);
}
