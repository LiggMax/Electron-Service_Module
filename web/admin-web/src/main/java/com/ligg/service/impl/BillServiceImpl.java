package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.query.OrderBillQuery;
import com.ligg.common.query.UserBillQuery;
import com.ligg.common.utils.CommissionUtils;
import com.ligg.common.vo.BillVo;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.OrderVo;
import com.ligg.common.vo.PageVo;
import com.ligg.mapper.adminweb.CustomerBillMapper;
import com.ligg.mapper.adminweb.OrderBillMapper;
import com.ligg.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

/**
 * @Author Ligg
 * @Time 2025/6/5
 **/
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private CustomerBillMapper customerBillMapper;

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Override
    public PageVo<CustomerBillVo> getUserBill(UserBillQuery userBill) {

        Page<CustomerBillVo> Page = new Page<>(userBill.getPageNum(), userBill.getPageSize());
        Page<CustomerBillVo> customerBillPage = customerBillMapper.selectCustomersBillPage(Page, userBill);

        // 封装返回数据
        PageVo<CustomerBillVo> pageVo = new PageVo<>();
        pageVo.setPages(customerBillPage.getPages());
        pageVo.setTotal(customerBillPage.getTotal());
        pageVo.setList(customerBillPage.getRecords());
        return pageVo;
    }

    @Override
    public PageVo<OrderBillEntity> getOrderBill(OrderBillQuery query) {
        // 获取所有订单账单数据
        Page<OrderBillEntity> page = new Page<>(query.getPageNum(), query.getPageSize());
        Page<OrderBillEntity> orderBillPage = orderBillMapper.selectOrderBillPage(page, query);

        PageVo<OrderBillEntity> pageVo = new PageVo<>();
        pageVo.setList(orderBillPage.getRecords());
        pageVo.setTotal(orderBillPage.getTotal());
        pageVo.setPages(orderBillPage.getPages());
        return pageVo;
    }

    /**
     * 获取订单详情
     *
     * @return 总流水 平台收益 卡商收益
     */
    @Override
    public BillVo getOrderDetail(YearMonth purchaseTime) {
        List<OrderBillEntity> orderBillEntities = orderBillMapper.selectDetailList(purchaseTime);

        float totalAmount = 0.0f; //总流水
        float totalPlatformProfit = 0.0f; //平台收益
        float totalMerchantProfit = 0.0f; //卡商收益

        //遍历计算汇总数据
        for (OrderBillEntity orderBill : orderBillEntities) {
            totalAmount += Optional.ofNullable(orderBill.getOrderMoney()).orElse(0.0f); //资金流水 = 所有订单金额总和
            totalMerchantProfit += Optional.ofNullable(orderBill.getRemainingAmount()).orElse(0.0f);//卡商利润 = 所有卡商收益总和
            totalPlatformProfit += Optional.ofNullable(orderBill.getCommissionAmount()).orElse(0.0f);//平台利润 = 所有平台收益总和
        }
        BillVo billVo = new BillVo();
        billVo.setAmount(totalAmount);
        billVo.setMerchantProfit(totalMerchantProfit);
        billVo.setPlatformProfit(totalPlatformProfit);
        return billVo;
    }
}
