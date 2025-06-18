package com.ligg.controller;

import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.query.OrderBillQuery;
import com.ligg.common.query.UserBillQuery;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.BillVo;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.OrderVo;
import com.ligg.common.vo.PageVo;
import com.ligg.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;


/**
 * @Author Ligg
 * @Time 2025/6/5
 * <p>
 * 账单
 **/

@RestController
@RequestMapping("/api/bill")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * 用户账单
     */
    @GetMapping("/user_bill")
    public Result<PageVo<CustomerBillVo>> getCustomerBill(@Validated UserBillQuery userBill) {
        PageVo<CustomerBillVo> userBillPage = billService.getUserBill(userBill);
        return Result.success(BusinessStates.SUCCESS, userBillPage);
    }

    /**
     * 订单账单
     */
    @GetMapping("/order_bill")
    public Result<PageVo<OrderBillEntity>> getOrderBill(@Validated OrderBillQuery orderBill) {
        PageVo<OrderBillEntity> orderBillPage = billService.getOrderBill(orderBill);
        return Result.success(BusinessStates.SUCCESS, orderBillPage);
    }

    /**
     * 订单详情信息
     * return BillVo
     */
    @GetMapping("/order_detail")
    public Result<BillVo> getOrderDetail(@DateTimeFormat(pattern = "yyyy-MM") YearMonth purchaseTime) {
        BillVo orderDetail = billService.getOrderDetail(purchaseTime);
        return Result.success(BusinessStates.SUCCESS, orderDetail);
    }
}
