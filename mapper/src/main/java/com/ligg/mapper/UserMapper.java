package com.ligg.mapper;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //根据账号密码查询用户信息
    AdminUserEntity findByAdminUser(String account, String password);

    //根据id查询用户信息
    AdminUserEntity findByAdminUserInfo(Long userId);

    //根据查询用户信息
    UserEntity findByUser(String account);

    //根据id查询用户信息
    UserEntity findByUserInfo(Long userId);
}
