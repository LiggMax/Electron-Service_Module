package com.ligg.controller;

import com.ligg.common.utils.Result;
import com.ligg.common.vo.OrderVo;
import com.ligg.service.adminweb.OrderService;
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
@RequestMapping("/api/adminWeb/order")
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
    public Result<String> settleOrder(Integer orderId){
        orderService.settleOrder(orderId);
        return Result.success(200,"结算成功");
    }
}
