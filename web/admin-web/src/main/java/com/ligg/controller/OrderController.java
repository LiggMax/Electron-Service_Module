package com.ligg.controller;

import com.ligg.common.entity.OrderEntity;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.statuEnum.OrderState;
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
    public Result<List<OrderVo>> getAllOrder() {
        return Result.success(BusinessStates.SUCCESS, orderService.getAllOrder());
    }

    /**
     * 结算订单
     */
    @PostMapping("/settle")
    public Result<String> settleOrder(String orderId) {
        //获取订单数据
        OrderEntity orderInfo = orderService.getOrderInfo(orderId);
        if (orderInfo == null) {
            return Result.error(BusinessStates.BAD_REQUEST, "订单不存在");
        }
        OrderState state = OrderState.fromCode(orderInfo.getState());
        if (state == null) {
            return Result.error(BusinessStates.BAD_REQUEST, "未知的订单状态");
        }

        return switch (state) {
            case UNUSED, IN_USE, SETTLED -> Result.error(BusinessStates.BAD_REQUEST, state.getMessage());
            case AVAILABLE_FOR_SETTLE -> {
                orderService.settleOrder(orderInfo);
                yield Result.success(BusinessStates.SUCCESS, "结算成功");
            }
        };
    }
}
