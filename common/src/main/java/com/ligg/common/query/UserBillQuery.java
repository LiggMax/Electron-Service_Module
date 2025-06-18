package com.ligg.common.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

/**
 * @Author Ligg
 * @Time 2025/6/17
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class UserBillQuery extends PageQuery {

    /**
     * 1:充值，2：消费
     */
    @Min(value = 1, message = "账单类型必须为1（充值）或2（消费）")
    @Max(value = 2, message = "账单类型必须为1（充值）或2（消费）")
    private Integer billType;

    /**
     * 0:客户，1：卡商
     */
    @Min(value = 0, message = "用户类型必须为0（客户）或1（卡商）")
    @Max(value = 1, message = "用户类型必须为0（客户）或1（卡商）")
    private Integer isUserType;

    /**
     * 时间（年月）
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth purchaseTime;
}
