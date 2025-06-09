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
 * 账单记录切面 - 统一处理购买和退款记录
 * @Author Ligg
 * @Time 2025/6/4
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

        LocalDateTime processTime = LocalDateTime.now();
        Long userId = extractUserId(args);

        log.info("📋 开始处理 {} - 用户ID: {}", remark, userId);

        try {
            Object result = joinPoint.proceed();

            // 根据不同的业务场景记录账单
            if (isRefundScenario(args)) {
                handleRefundBill(args, billAnnotation, processTime);
            } else if (isPurchaseScenario(result)) {
                handlePurchaseBill(result, userId, billAnnotation, processTime);
            }

            return result;
        } catch (Exception e) {
            log.error("💥 {} 处理异常 - 用户ID: {}, 错误: {}", remark, userId, e.getMessage(), e);
            throw e;
        } finally {
            log.info("📋 {} 处理完成", remark);
        }
    }

    /**
     * 判断是否为退款场景
     */
    private boolean isRefundScenario(Object[] args) {
        return args.length > 0 && args[0] instanceof OrderEntity;
    }

    /**
     * 判断是否为购买场景
     */
    private boolean isPurchaseScenario(Object result) {
        return result instanceof Map && Boolean.TRUE.equals(((Map<?, ?>) result).get("success"));
    }

    /**
     * 处理退款账单记录
     */
    private void handleRefundBill(Object[] args, Bill billAnnotation, LocalDateTime processTime) {
        OrderEntity orderInfo = (OrderEntity) args[0];

        log.info("💰 退款记录 - 用户ID: {}, 订单ID: {}, 退款金额: ¥{}",
                orderInfo.getUserId(),
                orderInfo.getOrdersId(),
                formatAmount(orderInfo.getProjectMoney()));

        // 记录退款账单
        saveBillRecord(orderInfo.getUserId(), orderInfo.getProjectMoney(),
                billAnnotation.isUserType(), billAnnotation.billType(),
                billAnnotation.remark(), processTime);
    }

    /**
     * 处理购买账单记录
     */
    private void handlePurchaseBill(Object result, Long userId, Bill billAnnotation, LocalDateTime processTime) {
        @SuppressWarnings("unchecked")
        Map<String, Object> resultMap = (Map<String, Object>) result;

        String orderId = (String) resultMap.get("orderId");
        Float totalCost = (Float) resultMap.get("totalCost");
        Float unitPrice = (Float) resultMap.get("unitPrice");
        Integer successCount = (Integer) resultMap.get("successCount");
        @SuppressWarnings("unchecked")
        List<String> purchasedPhones = (List<String>) resultMap.get("purchasedPhones");

        log.info("💰 购买记录 - 用户ID: {}, 订单ID: {}, 总金额: ¥{}, 数量: {}",
                userId, orderId, formatAmount(totalCost), successCount);

        // 为每个购买的号码记录账单
        if (purchasedPhones != null && unitPrice != null) {
            for (String phoneNumber : purchasedPhones) {
                log.debug("📱 记录号码账单 - 用户ID: {}, 手机号: {}, 单价: ¥{}",
                        userId, phoneNumber, formatAmount(unitPrice));

                saveBillRecord(userId, unitPrice, billAnnotation.isUserType(),
                        billAnnotation.billType(), billAnnotation.remark(), processTime);
            }
        }
    }

    /**
     * 保存账单记录到数据库
     */
    private void saveBillRecord(Long userId, Float amount, int isUserType, int billType,
                                String remark, LocalDateTime processTime) {
        try {
            CustomerBillEntity billEntity = new CustomerBillEntity();
            billEntity.setUserId(userId);
            billEntity.setAmount(amount);
            billEntity.setIsUserType(isUserType);
            billEntity.setBillType(billType);
            billEntity.setRemark(remark);
            billEntity.setPurchaseTime(processTime);

            billMapper.insert(billEntity);
            log.debug("✅ 账单记录已保存 - 用户ID: {}, 金额: ¥{}, 类型: {}",
                    userId, formatAmount(amount), getBillTypeDescription(billType));
        } catch (Exception e) {
            log.error("❌ 保存账单记录失败 - 用户ID: {}, 金额: ¥{}, 错误: {}",
                    userId, formatAmount(amount), e.getMessage(), e);
        }
    }

    /**
     * 智能提取userId
     */
    private Long extractUserId(Object[] args) {
        if (args.length == 0) {
            return null;
        }

        Object firstArg = args[0];
        if (firstArg instanceof Long) {
            return (Long) firstArg;
        } else if (firstArg instanceof OrderEntity) {
            return ((OrderEntity) firstArg).getUserId();
        } else {
            log.warn("未识别的参数类型: {}, 无法提取userId", firstArg.getClass().getSimpleName());
            return null;
        }
    }

    /**
     * 格式化金额显示
     */
    private String formatAmount(Float amount) {
        return amount != null ? String.format("%.2f", amount) : "0.00";
    }

    /**
     * 获取账单类型描述
     */
    private String getBillTypeDescription(int billType) {
        return switch (billType) {
            case 1 -> "增加";
            case 2 -> "减少";
            default -> "未知";
        };
    }
}