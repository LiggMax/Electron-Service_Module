package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.admin.MerchantEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminUserMapper extends BaseMapper<MerchantEntity> {
    //根据手机号查询卡商
    MerchantEntity selectByPhoneGetAdminUser(Long phoneNumber);
}
