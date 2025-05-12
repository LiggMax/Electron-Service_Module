package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("admin_users")
public class AdminUserEntity {
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    private String account;//账号
    private String nickName;
    @JsonIgnore
    private String password;
    private String email;
    private String userAvatar;
    private LocalDateTime createdAt;//创建时间
    private LocalDateTime updatedAt;//更新时间
}
