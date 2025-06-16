package com.ligg.service;

import com.ligg.common.entity.OrderEntity;

/**
 * @Author Ligg
 * @Time 2025/6/7
 **/
public interface CustomerOrdersService {

    /**
     * 根据id获取订单信息
     */
    OrderEntity getOrderById(String orderId);

    /**
     * 根据id删除订单
     */
    void deleteOrderById(String orderId);

    /**
     * 订单退款并回滚关联关系状态
     */
    void refundOrderAndUpdateRelation(OrderEntity orderInfo);

    /**
     * 根据订单id更新状态
     */
    void updateOrderStateById(String orderId, Integer state);
}
