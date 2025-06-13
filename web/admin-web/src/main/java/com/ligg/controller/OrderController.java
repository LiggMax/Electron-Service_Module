package com.ligg.controller;

import com.ligg.common.entity.OrderEntity;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.OrderVo;
import com.ligg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单管理
 */
@RestController
@RequestMapping("/api/admin_web/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 获取所有订单
     */
    @GetMapping
    public Result<List<OrderVo>> getAllOrder(){
        return Result.success(200,orderService.getAllOrder());
    }

    /**
     * 结算订单
     */
    @PostMapping("/settle")
    public Result<String> settleOrder(String orderId) {
        //获取订单数据
        OrderEntity orderInfo = orderService.getOrderInfo(orderId);
        if(orderInfo == null){
            return Result.error(400,"订单不存在");
        }
        if (orderInfo.getState() == 0){
            return Result.error(400,"订单还未使用，不可结算");
        }
        if (orderInfo.getState() == 2){
            return Result.error(400,"订单已结算,请勿重复提交");
        }
        //结算订单
        orderService.settleOrder(orderInfo);
        return Result.success(200,"结算成功");
    }
}
