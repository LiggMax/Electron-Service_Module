package com.ligg.common.entity;

import lombok.Data;

@Data
public class AdminWebUserEntity {

    private Long adminId;
    private String account;
    private String password;
    private String email;
    private String nickName;
}
