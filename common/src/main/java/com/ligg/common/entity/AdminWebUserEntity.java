package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_web_user")
public class AdminWebUserEntity {
    @TableId
    private Long adminId;
    private String account;
    private String password;
    private String email;
    private String nickName;
    private Long phoneNumber;//号码
    private LocalDateTime loginTime;//登录时间
}
