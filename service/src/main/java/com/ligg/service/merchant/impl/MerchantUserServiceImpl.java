package com.ligg.service.merchant.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.MerchantMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.merchant.MerchantUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantUserServiceImpl extends ServiceImpl<MerchantMapper, MerchantEntity> implements MerchantUserService {

    @Autowired
    private MerchantMapper adminUserMapper;
    @Autowired
    private UserOrderMapper userOrderMapper;

    /**
     * 重置密码
     */
    @Override
    public void resetPassword(Long userId, String password) {
        LambdaUpdateWrapper<MerchantEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MerchantEntity::getUserId, userId)
                .set(MerchantEntity::getPassword, BCryptUtil.encrypt(password));
        adminUserMapper.update(null, updateWrapper);
    }

    /**
     * 添加卡商
     */
    @Override
    public void saveCardUser(MerchantEntity merchantEntity) {
        merchantEntity.setCreatedAt(LocalDateTime.now());
        merchantEntity.setPassword(BCryptUtil.encrypt(merchantEntity.getPassword()));
        adminUserMapper.insert(merchantEntity);
    }

    /**
     * 更新登录时间
     */
    @Override
    public void updateLoginTime(Long userId) {
        LambdaUpdateWrapper<MerchantEntity> updateWrapper = new LambdaUpdateWrapper<MerchantEntity>()
                .eq(MerchantEntity::getUserId, userId)
                .set(MerchantEntity::getLoginTime, LocalDateTime.now());
        adminUserMapper.update(null, updateWrapper);
    }

    /**
     * 获取订单
     */
    @Override
    public List<OrderVo> getOrder(Long AdminId) {
        List<OrderEntity> orderEntities = userOrderMapper.selectList(new LambdaUpdateWrapper<OrderEntity>()
                .eq(OrderEntity::getMerchantId, AdminId));

        ArrayList<OrderVo> orderVoList = new ArrayList<>();
        for (OrderEntity orderEntity : orderEntities){
            OrderVo orderVo = new OrderVo();
            orderVo.setId(orderEntity.getOrdersId());
            orderVo.setAdminId(orderEntity.getMerchantId());
            orderVo.setUserId(orderEntity.getUserId());
            orderVo.setPhoneNumber(orderEntity.getPhoneNumber());
            orderVo.setState(orderEntity.getState());
            orderVo.setCreatedAt(orderEntity.getCreatedAt());
            orderVo.setPhoneMoney(orderEntity.getPhoneMoney());

            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
}
