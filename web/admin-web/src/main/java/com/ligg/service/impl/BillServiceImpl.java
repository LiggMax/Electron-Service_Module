package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.OrderBillVo;
import com.ligg.common.vo.BillVo;
import com.ligg.mapper.adminweb.CustomerBillMapper;
import com.ligg.mapper.adminweb.OrderBillMapper;
import com.ligg.service.BillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<CustomerBillVo> getCustomerBill() {
        List<CustomerBillVo> customerBill = customerBillMapper.selectCustomersBill();
//
//        //TODO 暂时将数据转换成vo，后续添加新数据
        List<CustomerBillVo> customerBillVoList = new ArrayList<>();
        for (CustomerBillVo entity : customerBill) {
            CustomerBillVo vo = new CustomerBillVo();
            BeanUtils.copyProperties(entity, vo);
            customerBillVoList.add(vo);
        }
        return customerBillVoList;
    }

    @Override
    public BillVo getOrderBill() {
        // 获取所有订单账单数据
        List<OrderBillEntity> orderBillList = orderBillMapper.selectList(new LambdaQueryWrapper<OrderBillEntity>()
                .orderByDesc(OrderBillEntity::getStartTime));

        // 转换为VO列表
        List<OrderBillVo> orderBillVoList = new ArrayList<>();

        // 初始化汇总数据
        float totalAmount = 0.0f;        // 资金总流水
        float totalMerchantProfit = 0.0f; // 卡商总利润
        float totalPlatformProfit = 0.0f; // 平台总利润

        // 遍历计算汇总数据并转换VO
        for (OrderBillEntity entity : orderBillList) {
            // 转换为VO
            OrderBillVo vo = new OrderBillVo();
            BeanUtils.copyProperties(entity, vo);
            orderBillVoList.add(vo);

            // 累计汇总数据
            if (entity.getOrderMoney() != null) {
                totalAmount += entity.getOrderMoney(); // 资金流水 = 所有订单金额总和
            }
            if (entity.getRemainingAmount() != null) {
                totalMerchantProfit += entity.getRemainingAmount(); // 卡商利润 = 所有卡商收益总和
            }
            if (entity.getCommissionAmount() != null) {
                totalPlatformProfit += entity.getCommissionAmount(); // 平台利润 = 所有平台收益总和
            }
        }

        // 构建返回的分层数据结构
        BillVo billVo = new BillVo();
        billVo.setAmount(totalAmount);              // 资金总流水
        billVo.setMerchantProfit(totalMerchantProfit); // 卡商总利润
        billVo.setPlatformProfit(totalPlatformProfit); // 平台总利润
        billVo.setOrderBills(orderBillVoList);         // 详细订单账单列表

        return billVo;
    }
}
