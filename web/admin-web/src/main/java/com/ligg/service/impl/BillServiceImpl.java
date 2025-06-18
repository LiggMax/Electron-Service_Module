package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.query.OrderBillQuery;
import com.ligg.common.query.UserBillQuery;
import com.ligg.common.vo.CustomerBillVo;
import com.ligg.common.vo.PageVo;
import com.ligg.mapper.adminweb.CustomerBillMapper;
import com.ligg.mapper.adminweb.OrderBillMapper;
import com.ligg.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
