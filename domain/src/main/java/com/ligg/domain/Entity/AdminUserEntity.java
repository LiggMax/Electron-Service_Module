package com.ligg.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminUserEntity {
    private String userId;
    private String account;//账号
    private String nickName;
    @JsonIgnore
    private String password;
    private String email;
    private String userAvatar;
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}
