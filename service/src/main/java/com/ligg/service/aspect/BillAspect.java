package com.ligg.service.aspect;

import com.ligg.common.entity.BillEntity;
import com.ligg.mapper.BillMapper;
import com.ligg.service.annotation.Bill;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Author Ligg
 * @Time 2025/6/4
 * 账单记录切面
 * 用于记录购买相关的关键信息
 */
@Slf4j
@Aspect
@Component
public class BillAspect {

    @Autowired
    private BillMapper billMapper;

    @Around("@annotation(com.ligg.service.annotation.Bill)")
    public Object recordBill(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 获取注解信息
        Bill billAnnotation = method.getAnnotation(Bill.class);
        String operation = billAnnotation.operation();

        // 记录购买时间
        LocalDateTime purchaseTime = LocalDateTime.now();

        // 提取userId（第一个参数）
        Long userId = args.length > 0 ? (Long) args[0] : null;

        log.info("=== 📋 {} 开始 ===", operation);

        try {
            // 执行原方法
            Object result = joinPoint.proceed();

            // 处理成功结果
            if (result instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> resultMap = (Map<String, Object>) result;

                if (Boolean.TRUE.equals(resultMap.get("success"))) {
                    // 提取关键账单信息
                    String orderId = (String) resultMap.get("orderId");
                    Float totalCost = (Float) resultMap.get("totalCost");
                    Float unitPrice = (Float) resultMap.get("unitPrice");
                    Integer successCount = (Integer) resultMap.get("successCount");
                    @SuppressWarnings("unchecked")
                    List<String> purchasedPhones = (List<String>) resultMap.get("purchasedPhones");

                    // 打印汇总信息
                    log.info("💰 账单汇总 - 用户ID: {}, 总金额: ¥{}, 主订单ID: {}, 购买时间: {}, 总数量: {}",
                            userId,
                            totalCost != null ? String.format("%.2f", totalCost) : "0.00",
                            orderId != null ? orderId : "",
                            purchaseTime,
                            successCount != null ? successCount : 0);

                    // 为每个购买的号码记录单独的账单数据
                    if (purchasedPhones != null && unitPrice != null) {
                        for (int i = 0; i < purchasedPhones.size(); i++) {
                            String phoneNumber = purchasedPhones.get(i);
                            // 为每个号码生成唯一的子订单ID
                            String subOrderId = orderId + "_" + (i + 1);

                            // 记录每个号码的详细账单
                            log.info("📱 单号码账单 - 用户ID: {}, 手机号: {}, 单价: ¥{}, 子订单ID: {}, 购买时间: {}",
                                    userId,
                                    phoneNumber,
                                    String.format("%.2f", unitPrice),
                                    subOrderId,
                                    purchaseTime);

                            // 记录账单到数据库
                            BillEntity billEntity = new BillEntity();
                            billEntity.setUserId(userId);
                            billEntity.setAmount(unitPrice);
                            billEntity.setIsUserType(0);
                            billEntity.setBillType(1);
                            billEntity.setPurchaseTime(purchaseTime);
                            billMapper.insert(billEntity);
                        }
                    }
                } else {
                    String errorMessage = (String) resultMap.get("error");
                    log.warn("❌ {} 失败 - 用户ID: {}, 原因: {}, 时间: {}", operation, userId, errorMessage, purchaseTime);
                }
            }

            return result;

        } catch (Exception e) {
            log.error("💥 {} 异常 - 用户ID: {}, 错误: {}, 时间: {}", operation, userId, e.getMessage(), purchaseTime);
            throw e;
        } finally {
            log.info("=== 📋 {} 结束 ===", operation);
        }
    }
}