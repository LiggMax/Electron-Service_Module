package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.mapper.MerchantMapper;
import com.ligg.service.AdminMerchantUserService;
import com.ligg.service.annotation.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/6/2
 * <p>
 * 卡商管理
 **/
@Service
public class AdminMerchantUserServiceImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements AdminMerchantUserService {

    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 添加卡商
     */
    @Override
    public void saveCardUser(MerchantEntity merchantEntity) {
        merchantEntity.setCreatedAt(LocalDateTime.now());
        merchantEntity.setPassword(BCryptUtil.encrypt(merchantEntity.getPassword()));
        merchantMapper.insert(merchantEntity);
    }

    /**
     * 重置密码
     */
    @Override
    public void resetPassword(Long userId, String password) {
        LambdaUpdateWrapper<MerchantEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MerchantEntity::getUserId, userId)
                .set(MerchantEntity::getPassword, BCryptUtil.encrypt(password));
        merchantMapper.update(null, updateWrapper);
    }

    /**
     * 修改卡商信息
     */
    @Override
    public void updateEditById(MerchantEntity merchantEntity) {
        merchantMapper.updateById(merchantEntity);
    }


    /**
     * 提现
     */
    @Bill(remark = "卡商提现", isUserType = 1)
    @Override
    public void payouts(Long userId, Float balance, Boolean isType) {
        if (!isType) {
            //TODO 后续实现充值功能
            return;
        } else {
            merchantMapper.payouts(userId, balance);
        }
    }
}
