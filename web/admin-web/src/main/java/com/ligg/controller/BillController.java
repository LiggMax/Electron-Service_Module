package com.ligg.controller;

import com.ligg.common.utils.Result;
import com.ligg.common.vo.BillVo;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.OrderBillVo;
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
     * 获取账单
     */
    @GetMapping("/customerBill")
    public Result<Map<String, Object>> getCustomerBill() {
        //客户账单
        List<CustomerBillVo> customerBill = billService.getCustomerBill();
        //订单账单
        BillVo orderBill = billService.getOrderBill();

        HashMap<String, Object> billMap = new HashMap<>();
        billMap.put("customerBill", customerBill);
        billMap.put("orderBill", orderBill);
        return Result.success(200, billMap);
    }
}
