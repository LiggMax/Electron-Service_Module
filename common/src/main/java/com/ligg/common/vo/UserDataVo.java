package com.ligg.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDataVo {
    private Long userId;
    private String nickName;
    private String oldPassword;//旧密码
    private String newPassword;//新密码
    private String userAvatar;
}
