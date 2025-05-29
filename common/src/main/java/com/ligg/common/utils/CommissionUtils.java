package com.ligg.common.utils;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author by Ligg
 * @Time 2025/5/29
 **/
public class CommissionUtils {

    /**
     * 返回包含抽成金额和抽成后金额的对象
     */
    @Getter
    public static class CommissionResult {
        private final BigDecimal commissionAmount; // 被抽成掉的金额
        private final BigDecimal remainingAmount;  // 抽成后实际到账金额

        public CommissionResult(BigDecimal commissionAmount, BigDecimal remainingAmount) {
            this.commissionAmount = commissionAmount;
            this.remainingAmount = remainingAmount;
        }

    }

    /**
     * 根据订单价格和卡商抽成比例计算抽成金额与剩余金额
     *
     * @param percentage 卡商抽成比例（整数，如 5 表示 5%）
     * @param orderPrice 订单价格（浮点数）
     * @return CommissionResult 包含抽成金额和剩余金额
     */
    public static CommissionResult calculateCommission(Integer percentage, Float orderPrice) {
        if (percentage == null || orderPrice == null) {
            throw new IllegalArgumentException("抽成比例和订单价格不能为空");
        }

        BigDecimal percentDecimal = BigDecimal.valueOf(percentage);
        BigDecimal priceDecimal = BigDecimal.valueOf(orderPrice);

        // 抽成金额 = 订单价格 * (抽成比例 / 100)
        BigDecimal commission = priceDecimal.multiply(percentDecimal)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        // 剩余金额 = 订单价格 - 抽成金额
        BigDecimal remaining = priceDecimal.subtract(commission)
                .setScale(2, RoundingMode.HALF_UP);

        return new CommissionResult(commission, remaining);
    }
}
