package com.ligg.controller;

import com.ligg.common.query.CustomerBillQuery;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.BillVo;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.PageVo;
import com.ligg.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result<PageVo<CustomerBillVo>> getCustomerBill(CustomerBillQuery customerBill) {
        PageVo<CustomerBillVo> userBillPage = billService.getUserBill(customerBill);
        return Result.success(BusinessStates.SUCCESS, userBillPage);
    }

    /**
     * 订单账单
     */
    @GetMapping("/order_bill")
    public Result<BillVo> getOrderBill() {
        BillVo orderBill = billService.getOrderBill();
        return Result.success(BusinessStates.SUCCESS, orderBill);
    }
}
