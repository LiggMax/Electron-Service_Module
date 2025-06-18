package com.ligg.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/5
 **/
@Data
public class BillVo {

    /**
     * 资金流水
     */
    private Float amount;

    /**
     * 卡商利润
     */
    private Float merchantProfit;

    /**
     * 平台利润
     */
    private Float platformProfit;
}
