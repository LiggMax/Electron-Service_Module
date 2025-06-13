package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.admin.MerchantEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * @Author Ligg
 * @Time 2025/6/2
 **/
public interface AdminMerchantUserService extends IService<MerchantEntity> {
    //添加卡商
    void saveCardUser(MerchantEntity merchantEntity);

    //重置密码
    void resetPassword(Long userId, @Min(value = 6, message = "密码长度不能小于6位") @Max(value = 20, message = "密码长度不能大于20位") String password);

    //修改卡商信息
    void updateEditById(MerchantEntity merchantEntity);

    /**
     * 提现
     *
     * @param isType true:提现 false:充值
     */
    void payouts(Long userId, Float balance, Boolean isType);
}
