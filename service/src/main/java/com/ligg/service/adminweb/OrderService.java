package com.ligg.service.adminweb;


import com.ligg.common.entity.OrderEntity;
import com.ligg.common.vo.OrderVo;

import java.util.List;

public interface OrderService {
    //获取所有订单
    List<OrderVo> getAllOrder();
    //结算订单
    void settleOrder(OrderEntity order);
    //获取订单详情
    OrderEntity getOrderInfo(Integer orderId);
}
