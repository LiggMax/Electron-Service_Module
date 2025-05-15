package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_web_user")
public class AdminWebUserEntity {
    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long adminId;
    private String account;
    private String password;
    @Email
    private String email;
    private String nickName;
    private Long phoneNumber;//号码
    private LocalDateTime loginTime;//登录时间
    private String loginIp;//登录ip
}
