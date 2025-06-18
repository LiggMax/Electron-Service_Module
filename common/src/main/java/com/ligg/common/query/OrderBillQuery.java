package com.ligg.common.query;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.YearMonth;

/**
 * @Author Ligg
 * @Time 2025/6/18
 **/
@Getter
@Setter
public class OrderBillQuery extends PageQuery {


    /**
     * 时间（年月）
     */
    @DateTimeFormat(pattern = "yyyy-MM")
    private YearMonth purchaseTime;
}
