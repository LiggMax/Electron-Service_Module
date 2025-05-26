package com.ligg.common.entity.admin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_users")
public class AdminUserEntity {
    @TableId
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
    @Pattern(regexp = "^[a-zA-Z\\d]{6,20}$", message = "密码必须为6~20位的字母或数字")
    private String account;//账号
    private String nickName;
    @JsonIgnore
    @Pattern(regexp = "^[a-zA-Z\\d]{6,20}$", message = "密码必须为6~20位的字母或数字")
    private String password;
    @Email
    private String email;
    private String userAvatar;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long phoneNumber;
    private Float money;//卡商余额
    private LocalDateTime loginTime;//登录时间
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}
