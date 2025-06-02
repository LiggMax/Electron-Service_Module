package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.vo.OrderVo;

import java.util.List;

public interface MerchantUserService extends IService<MerchantEntity> {
    //根据账号查村用户数据
    MerchantEntity getUserByAccount(String account);
    //更新登录时间
    void updateLoginTime(Long userId);
    //获取订单
    List<OrderVo> getOrder(Long AdminId);

    //根据id查询用户
    MerchantEntity getUserById(Long userId);
}
