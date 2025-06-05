package com.ligg.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/6/5
 **/
@Data
public class CustomerBillVo {
    private String id;

    /**
     * 1:充值，2：消费
     */
    private Integer billType;

    /**
     * 金额
     */
    private Float amount;
    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 1:客户，2：卡商
     */
    private Integer isUserType;

    /**
     * 购买时间
     */
    private LocalDateTime purchaseTime;
}
