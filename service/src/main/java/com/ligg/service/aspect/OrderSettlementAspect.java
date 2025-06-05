package com.ligg.service.aspect;

import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.utils.CommissionUtils;
import com.ligg.common.utils.JWTUtil;
import com.ligg.mapper.AdminWeb.OrderBillMapper;
import com.ligg.mapper.MerchantMapper;
import com.ligg.service.annotation.OrderSettlement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author Ligg
 * @Time 2025/1/15
 * 订单结算记录切面
 * 用于记录订单结算的关键信息
 */
@Slf4j
@Aspect
@Component
public class OrderSettlementAspect {

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private OrderBillMapper orderBillMapper;

    @Around("@annotation(com.ligg.service.annotation.OrderSettlement)")
    public Object recordOrderSettlement(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 获取注解信息
        OrderSettlement orderSettlement = method.getAnnotation(OrderSettlement.class);
        String operation = orderSettlement.operation();
        boolean enableLog = orderSettlement.enableLog();

        if (!enableLog) {
            return joinPoint.proceed();
        }

        // 记录开始时间
        LocalDateTime startTime = LocalDateTime.now();
        log.info("=== 🏦 {} 开始 === 时间: {}", operation, startTime);

        try {
            // 提取订单信息（第一个参数应该是OrderEntity）
            OrderEntity order = args.length > 0 ? (OrderEntity) args[0] : null;

            if (order != null) {
                // 计算订单金额
                Float orderMoney = order.getProjectMoney();

                // 获取卡商抽成比例
                MerchantEntity merchantInfo = merchantMapper.selectById(order.getMerchantId());

                // 计算抽成金额和剩余金额
                CommissionUtils.CommissionResult result = CommissionUtils.calculateCommission(
                        merchantInfo.getDivideInto(), orderMoney);

                BigDecimal remainingAmount = result.getRemainingAmount(); // 剩余金额（卡商收益）
                BigDecimal commissionAmount = result.getCommissionAmount(); // 被抽成掉的金额（平台收益）

                // 获取平台管理员ID
                Map<String, Object> adminWebUserInfo = jwtUtil.parseToken(request.getHeader("Token"));
                Long officialId = (Long) adminWebUserInfo.get("userId");

                // 打印订单结算详细信息
                log.info("💰 订单结算详情:");
                log.info("  📄 订单ID: {}", order.getOrdersId());
                log.info("  💵 订单总金额: ¥{}", String.format("%.2f", orderMoney));
                log.info("  📊 抽成比例: {}%", merchantInfo.getDivideInto());
                log.info("  🏪 卡商ID: {}", order.getMerchantId());
                log.info("  💎 卡商收益(剩余金额): ¥{}", remainingAmount);
                log.info("  🏢 平台ID: {}", officialId);
                log.info("  💰 平台收益(抽成金额): ¥{}", commissionAmount);
                log.info("  ⏰ 结算时间: {}", startTime);

                // 保存订单结算记录
                OrderBillEntity orderBill = new OrderBillEntity();
                orderBill.setOrderId(order.getOrdersId());
                orderBill.setOrderMoney(orderMoney);
                orderBill.setDivideInto(merchantInfo.getDivideInto());
                orderBill.setMerchantId(order.getMerchantId());
                orderBill.setAdminId(order.getMerchantId());
                orderBill.setRemainingAmount(remainingAmount.floatValue());
                orderBill.setAdminId(officialId);
                orderBill.setCommissionAmount(commissionAmount.floatValue());
                orderBill.setStartTime(startTime);
                orderBillMapper.insert(orderBill);
            }

            // 执行原方法
            Object result = joinPoint.proceed();

            LocalDateTime endTime = LocalDateTime.now();
            log.info("✅ {} 成功完成 - 结束时间: {}", operation, endTime);

            return result;

        } catch (Exception e) {
            LocalDateTime errorTime = LocalDateTime.now();
            log.error("❌ {} 执行失败 - 错误时间: {}, 错误信息: {}", operation, errorTime, e.getMessage());
            throw e;
        } finally {
            log.info("=== 📋 {} 结束 ===", operation);
        }
    }
} 