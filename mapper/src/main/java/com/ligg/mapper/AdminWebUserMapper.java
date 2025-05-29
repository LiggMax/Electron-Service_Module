package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.lang.constant.ConstantDesc;
import java.math.BigDecimal;

@Mapper
public interface AdminWebUserMapper extends BaseMapper<AdminWebUserEntity> {

    //平台结算金额
    void officialAmountSettlement(ConstantDesc officialId, BigDecimal commissionAmount);
}
