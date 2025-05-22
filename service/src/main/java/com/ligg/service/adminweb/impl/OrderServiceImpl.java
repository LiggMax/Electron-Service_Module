package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ligg.common.entity.AccountFundsEntity;
import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.UserOrderEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.vo.OrderVo;
import com.ligg.mapper.AdminUserMapper;
import com.ligg.mapper.AdminWeb.AccountFundsMapper;
import com.ligg.mapper.AdminWeb.OrderMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.adminweb.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Autowired
    private AccountFundsMapper accountFundsMapper;

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

    /**
     * 结算订单
     */
    @Override
    public void settleOrder(Integer orderId) {
        //获取订单数据
        UserOrderEntity orderEntity = userOrderMapper.selectById(orderId);
        //根据号码获取卡商数据
        AdminUserEntity adminUser = adminUserMapper.selectByPhoneGetAdminUser(orderEntity.getPhoneNumber());

        //结算卡商余额
        adminUserMapper.update(new LambdaUpdateWrapper<AdminUserEntity>()
                .eq(AdminUserEntity::getUserId,adminUser.getUserId())
                .set(AdminUserEntity::getMoney,orderEntity.getPhoneMoney()));

        //结算平台余额
        Map<String, Object> AdminWebUserInfo = jwtUtil.parseToken(request.getHeader("Token"));
        Long adminWebUserId = (Long) AdminWebUserInfo.get("userId");
        accountFundsMapper.update(new LambdaUpdateWrapper<AccountFundsEntity>()
                .eq(AccountFundsEntity::getAccountId,adminWebUserId)
                .set(AccountFundsEntity::getMoney,orderEntity.getProjectMoney()));
    }
}
