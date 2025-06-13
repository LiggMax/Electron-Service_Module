package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.admin.MerchantEntity;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;

@Mapper
public interface MerchantMapper extends BaseMapper<MerchantEntity> {
    //根据手机号查询卡商
    MerchantEntity selectByPhoneGetAdminUser(Long phoneNumber);

    //卡商结算
    void amountSettlement(Long userId, BigDecimal remainingAmount);

    //卡商提现
    void payouts(Long userId, Float balance);
}
