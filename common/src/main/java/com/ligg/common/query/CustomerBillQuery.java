package com.ligg.common.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/6/17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerBillQuery extends PageQuery {

    /**
     * 1:充值，2：消费
     */
    @Pattern(regexp = "^([12])$", message = "用户类型必须为1（充值）或2（消费）")
    private Integer billType;

    /**
     * 1:客户，2：卡商
     */
    @Pattern(regexp = "^([12])$", message = "用户类型必须为1（客户）或2（卡商）")
    private Integer isUserType;

    /**
     * 购买时间
     */
    private LocalDateTime purchaseTime;
}
