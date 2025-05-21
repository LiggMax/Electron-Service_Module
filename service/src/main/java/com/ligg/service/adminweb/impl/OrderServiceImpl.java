package com.ligg.service.adminweb.impl;

import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.AdminWeb.OrderMapper;
import com.ligg.service.adminweb.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    /**
     * 获取所有订单
     */
    @Override
    public List<OrderVo> getAllOrder() {
        List<OrderVo> orders = orderMapper.getAllOrder();
        return orders;
    }
}
