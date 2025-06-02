package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.utils.CommissionUtils;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.MerchantMapper;
import com.ligg.mapper.AdminWeb.OrderMapper;
import com.ligg.mapper.AdminWebUserMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private AdminWebUserMapper adminWebUserMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 获取所有订单
     */
    @Override
    public List<OrderVo> getAllOrder() {
        return orderMapper.getAllOrder();
    }


    @Override
    public OrderEntity getOrderInfo(Integer orderId) {
        return userOrderMapper.selectById(orderId);
    }

    /**
     * 结算订单
     */
    @Override
    @Transactional
    public void settleOrder(OrderEntity order) {

        //订单价格
        Float orderMoney = order.getPhoneMoney() + order.getProjectMoney();

        // 获取卡商抽成比例
        MerchantEntity merchantInfo = merchantMapper.selectById(order.getMerchantId());
        log.info("获取卡商抽成比例 {}%", merchantInfo.getDivideInto());

        // 计算抽成金额和剩余金额
        CommissionUtils.CommissionResult result = CommissionUtils.calculateCommission(merchantInfo.getDivideInto(), orderMoney);

        BigDecimal remainingAmount = result.getRemainingAmount(); //剩余金额（卡商收益）
        BigDecimal commissionAmount = result.getCommissionAmount(); //被抽成掉的金额（平台收益）

        //结算卡商余额：+ 抽成后的金额
        merchantMapper.amountSettlement(merchantInfo.getUserId(), remainingAmount);
        log.info("结算订单，卡商金额：+{}", remainingAmount);

        //结算平台余额
        Map<String, Object> AdminWebUserInfo = jwtUtil.parseToken(request.getHeader("Token"));
        Long officialId = (Long) AdminWebUserInfo.get("userId");
        adminWebUserMapper.officialAmountSettlement(officialId, commissionAmount);
        log.info("结算订单，平台金额：+{}", commissionAmount);

        //更新订单状态
        userOrderMapper.update(new LambdaUpdateWrapper<OrderEntity>()
                .eq(OrderEntity::getOrdersId, order.getOrdersId())
                .set(OrderEntity::getState, 2));
        log.info("结算订单，更新订单状态为以结算");
    }
}
