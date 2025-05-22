package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_order")
public class UserOrderEntity {
    @TableId(type = IdType.AUTO)
    private Integer userProjectId;
    private Long userId;
    private LocalDateTime createdAt;
    private Integer projectId;
    private Long phoneNumber;
    private Float projectMoney;//项目金额
    private Float phoneMoney;//号码金额
    private Integer state;//状态
    private Integer code;//验证码
}
