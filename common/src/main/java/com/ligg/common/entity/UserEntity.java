package com.ligg.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    private Long userId;
    private String account;
    @JsonIgnore
    private String password;
    private String nickName;
    private String email;
    private String userAvatar;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userStatus; // 1: 正常 0: 注销
}
