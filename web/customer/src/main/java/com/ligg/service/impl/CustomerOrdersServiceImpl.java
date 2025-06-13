package com.ligg.service.impl;

import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.PhoneProjectRelationMapper;
import com.ligg.mapper.user.CustomerMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.CustomerOrdersService;
import com.ligg.service.annotation.Bill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Ligg
 * @Time 2025/6/7
 **/
@Service
public class CustomerOrdersServiceImpl implements CustomerOrdersService {

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private PhoneProjectRelationMapper phoneProjectRelationMapper;
    /**
     * 根据id获取订单信息
     */
    @Override
    public OrderEntity getOrderById(String orderId) {
        return userOrderMapper.selectById(orderId);
    }

    /**
     * 根据id删除订单
     */
    @Override
    public void deleteOrderById(String orderId) {
        userOrderMapper.deleteById(orderId);
    }

    /**
     * 订单退款并回滚关联关系状态
     */
    @Bill(remark = "订单超时未使用退款", billType = 1)
    @Override
    public void refundOrderAndUpdateRelation(OrderEntity orderInfo) {
        if (orderInfo.getState() == 0) {
            //退款
            customerMapper.addUserMoney(orderInfo.getUserId(), orderInfo.getProjectMoney());
            //回滚号码项目关联状态为可用
            PhoneEntity phone = phoneNumberMapper.getPhoneByNumber(orderInfo.getPhoneNumber());
            phoneProjectRelationMapper.rollbackAvailableStatus(phone.getPhoneId(), orderInfo.getProjectId());
        }
    }
}
