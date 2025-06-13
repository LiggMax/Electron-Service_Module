package com.ligg.service;

import com.ligg.common.vo.BillVo;
import com.ligg.common.vo.CustomerBillVo;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/5
 **/
public interface BillService {

    /**
     * 获取客户账单
     */
    List<CustomerBillVo> getCustomerBill();

    /**
     * 订单账单 - 返回包含汇总数据的分层结构
     * 包含资金总流水、卡商利润、平台利润以及详细订单列表
     */
    BillVo getOrderBill();
}
