package com.ligg.mapper;

import com.ligg.domain.Entity.AdminUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //根据账号密码查询用户信息
    AdminUserEntity findByUser(String account,String password);
}
