package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("orders")
public class OrderEntity {
    @TableId(type = IdType.NONE)
    private String ordersId;
    private Long userId;
    private LocalDateTime createdAt;
    private Integer projectId;
    private Long phoneNumber;
    private Long merchantId;//卡商id
    private Float projectMoney;//项目金额
    private Integer state;//状态
    private String code;//验证码
}
