package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.AdminUserMapper;
import com.ligg.mapper.AdminWeb.OrderMapper;
import com.ligg.mapper.AdminWebUserMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.adminweb.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private AdminUserMapper adminUserMapper;

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

        // 结算卡商余额
        log.info("结算订单，更新卡商余额：+{}元", order.getPhoneMoney());
        adminUserMapper.update(new UpdateWrapper<MerchantEntity>()
                .eq("user_id", order.getAdminId())
                .setSql("money=money+" + order.getPhoneMoney()));

        //结算平台余额
        Map<String, Object> AdminWebUserInfo = jwtUtil.parseToken(request.getHeader("Token"));
        Long adminWebUserId = (Long) AdminWebUserInfo.get("userId");
        log.info("结算订单，更新平台余额：+{}元", order.getProjectMoney());
        adminWebUserMapper.update(new UpdateWrapper<AdminWebUserEntity>()
                .eq("admin_id", adminWebUserId)
                .setSql("money=money+" + order.getProjectMoney()));

        //更新订单状态
        log.info("结算订单，更新订单状态：{}", 2);
        userOrderMapper.update(new LambdaUpdateWrapper<OrderEntity>()
                .eq(OrderEntity::getOrderId, order.getOrderId())
                .set(OrderEntity::getState, 2));
    }
}
