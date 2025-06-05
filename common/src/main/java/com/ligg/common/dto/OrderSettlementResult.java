package com.ligg.common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Ligg
 * @Time 2025/1/15
 * 订单结算结果DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSettlementResult {

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 订单总金额
     */
    private Float orderMoney;

    /**
     * 剩余金额（卡商收益）
     */
    private BigDecimal remainingAmount;

    /**
     * 被抽成掉的金额（平台收益）
     */
    private BigDecimal commissionAmount;

    /**
     * 卡商ID
     */
    private Long merchantId;

    /**
     * 平台ID
     */
    private Long officialId;

    /**
     * 抽成比例
     */
    private Float divideInto;

    /**
     * 结算时间
     */
    private LocalDateTime settlementTime;
} 