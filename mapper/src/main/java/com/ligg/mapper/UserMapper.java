package com.ligg.mapper;

import com.ligg.common.entity.AdminUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //根据账号密码查询用户信息
    AdminUserEntity findByAdminUser(String account, String password);
}
