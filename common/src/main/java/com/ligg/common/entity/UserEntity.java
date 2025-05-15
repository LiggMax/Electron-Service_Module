package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class UserEntity {
    @TableId
    private Long userId;
    private String account;
    @JsonIgnore
    private String password;
    //  昵称长度校验
    @Pattern(regexp = "^.{1,20}$")
    private String nickName;
    @Email
    private String email;
    private String userAvatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime loginTime;//登录时间
    private Integer userStatus; // 1: 正常 0: 注销
}
