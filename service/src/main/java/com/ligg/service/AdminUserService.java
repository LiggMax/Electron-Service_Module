package com.ligg.service;

import com.ligg.domain.Entity.AdminUserEntity;

public interface AdminUserService {

    //根据账号和密码查询用户
    AdminUserEntity findByUser(String account, String password);
}
