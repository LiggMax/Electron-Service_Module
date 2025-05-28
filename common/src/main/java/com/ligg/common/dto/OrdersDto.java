package com.ligg.common.dto;

import lombok.Data;

/**
 * @Author by Ligg
 * @Time 2025/5/28
 **/

@Data
public class OrdersDto {
    private Integer ordersId;//订单id
    private String userName;
    private String adminName;
    private String projectName;
    private Long phoneNumber;
    private String code;
    private Integer state;
    private Float projectMoney;
    private Float phoneMoney;
}
