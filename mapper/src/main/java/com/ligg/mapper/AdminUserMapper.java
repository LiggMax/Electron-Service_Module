package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.admin.AdminUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUserEntity> {
    //根据手机号查询卡商
    AdminUserEntity selectByPhoneGetAdminUser(Long phoneNumber);
}
