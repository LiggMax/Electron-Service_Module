package com.ligg.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVo {
    private String id;
    private Long userId;//客户id
    private Long AdminId;//卡商id
    private String projectName;//项目名称
    private Long phoneNumber; //号码
    private Float projectMoney;//项目金额
    private String userNickName;//客户名称
    private String userAvatar;//客户头像
    private String adminNickName;//卡商名称
    private String adminAvatar;//卡商头像
    private String code;//验证码短信
    private Integer state;//订单状态
    private LocalDateTime createdAt;//订单时间
}
