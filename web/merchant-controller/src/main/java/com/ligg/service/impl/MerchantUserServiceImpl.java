package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.MerchantMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.MerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantUserServiceImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements MerchantUserService {

    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private UserOrderMapper userOrderMapper;

    /**
     * 账号获取卡商数据
     */
    @Override
    public MerchantEntity getUserByAccount(String account) {
        return merchantMapper.selectOne(new LambdaQueryWrapper<MerchantEntity>()
                .eq(MerchantEntity::getAccount, account));
    }

    /**
     * 根据id获取卡商数据
     */
    @Override
    public MerchantEntity getUserById(Long userId) {
        return merchantMapper.selectById(userId);
    }

    /**
     * 更新登录时间
     */
    @Override
    public void updateLoginTime(Long userId) {
        LambdaUpdateWrapper<MerchantEntity> updateWrapper = new LambdaUpdateWrapper<MerchantEntity>()
                .eq(MerchantEntity::getUserId, userId)
                .set(MerchantEntity::getLoginTime, LocalDateTime.now());
        merchantMapper.update(null, updateWrapper);
    }

    /**
     * 获取订单
     */
    @Override
    public List<OrderVo> getOrder(Long AdminId) {
        List<OrderEntity> orderEntities = userOrderMapper.selectList(new LambdaUpdateWrapper<OrderEntity>()
                .eq(OrderEntity::getMerchantId, AdminId)
                .orderByDesc(OrderEntity::getCreatedAt));

        ArrayList<OrderVo> orderVoList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities) {
            OrderVo orderVo = new OrderVo();
            orderVo.setId(orderEntity.getOrdersId());
            orderVo.setAdminId(orderEntity.getMerchantId());
            orderVo.setUserId(orderEntity.getUserId());
            orderVo.setPhoneNumber(orderEntity.getPhoneNumber());
            orderVo.setState(orderEntity.getState());
            orderVo.setCreatedAt(orderEntity.getCreatedAt());

            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
}
