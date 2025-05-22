package com.ligg.service.adminweb;


import com.ligg.common.vo.OrderVo;

import java.util.List;

public interface OrderService {
    //获取所有订单
    List<OrderVo> getAllOrder();
    //结算订单
    void settleOrder(Integer orderId);
}
