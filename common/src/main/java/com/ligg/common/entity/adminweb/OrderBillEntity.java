package com.ligg.common.entity.adminweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/6/5
 * <p>
 * 订单账单
 **/
@Data
@TableName("order_bill")
public class OrderBillEntity {

    @TableId(type = IdType.AUTO)
    private Integer Id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 卡商id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long merchantId;

    /**
     * 平台id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long adminId;

    /**
     * 抽成比例
     */
    private Integer divideInto;

    /**
     * 卡商收益
     */
    private Float remainingAmount;

    /**
     * 平台收益(抽成金额)
     */
    private Float commissionAmount;

    /**
     * 订单金额
     */
    private Float orderMoney;

    /**
     * 结算时间
     */
    private LocalDateTime startTime;
}
