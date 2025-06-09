package com.ligg.service.aspect;

import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.adminweb.CustomerBillEntity;
import com.ligg.mapper.adminweb.CustomerBillMapper;
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
 *
 * 账单记录切面
 * 用于记录购买相关的关键信息
 */
@Slf4j
@Aspect
@Component
public class BillAspect {

    @Autowired
    private CustomerBillMapper billMapper;

    @Around("@annotation(com.ligg.service.annotation.Bill)")
    public Object recordBill(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();

        // 获取注解信息
        Bill billAnnotation = method.getAnnotation(Bill.class);
        String remark = billAnnotation.remark();
        int isUserType = billAnnotation.isUserType();
        int billType = billAnnotation.billType();

        // 记录购买时间
        LocalDateTime purchaseTime = LocalDateTime.now();

        // 智能提取userId - 支持不同参数类型
        Long userId = extractUserId(args);

        try {
            // 执行原方法
            Object result = joinPoint.proceed();

            // 特殊处理退款操作（OrderEntity参数）
            if (args.length > 0 && args[0] instanceof OrderEntity orderInfo) {

                log.info("💰 订单退款记录 - 用户ID: {}, 订单ID: {}, 退款金额: ¥{}, 处理时间: {}",
                        orderInfo.getUserId(),
                        orderInfo.getOrdersId(),
                        orderInfo.getProjectMoney() != null ? String.format("%.2f", orderInfo.getProjectMoney()) : "0.00",
                        purchaseTime);

                // 记录退款账单到数据库
                CustomerBillEntity billEntity = new CustomerBillEntity();
                billEntity.setUserId(orderInfo.getUserId());
                billEntity.setAmount(orderInfo.getProjectMoney());
                billEntity.setIsUserType(isUserType);
                billEntity.setBillType(billType);
                billEntity.setRemark(remark);
                billEntity.setPurchaseTime(purchaseTime);
                billMapper.insert(billEntity);

                log.info("✅ 退款账单已记录到数据库");
                return result;
            }

            // 处理其他业务逻辑的返回结果（原有购买逻辑）
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
                            CustomerBillEntity billEntity = new CustomerBillEntity();
                            billEntity.setUserId(userId);
                            billEntity.setAmount(unitPrice);
                            billEntity.setIsUserType(isUserType);
                            billEntity.setBillType(billType);
                            billEntity.setRemark(remark);
                            billEntity.setPurchaseTime(purchaseTime);
                            billMapper.insert(billEntity);
                        }
                    }
                } else {
                    String errorMessage = (String) resultMap.get("error");
                    log.warn("❌ {} 失败 - 用户ID: {}, 原因: {}, 时间: {}", remark, userId, errorMessage, purchaseTime);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("💥 {} 异常 - 用户ID: {}, 错误: {}, 时间: {}", remark, userId, e.getMessage(), purchaseTime);
            throw e;
        } finally {
            log.info("=== 📋 {} 结束 ===", remark);
        }
    }

    /**
     * 智能提取userId - 支持多种参数类型
     *
     * @param args 方法参数数组
     * @return userId
     */
    private Long extractUserId(Object[] args) {
        if (args.length == 0) {
            return null;
        }

        Object firstArg = args[0];

        // 如果第一个参数是Long类型，直接返回（通常是购买场景）
        if (firstArg instanceof Long) {
            return (Long) firstArg;
        }
        // 如果第一个参数是OrderEntity，提取其中的userId（退款场景）
        else if (firstArg instanceof OrderEntity) {
            return ((OrderEntity) firstArg).getUserId();
        } else {
            log.warn("未识别的参数类型: {}, 无法提取userId", firstArg.getClass().getSimpleName());
            return null;
        }
    }
}