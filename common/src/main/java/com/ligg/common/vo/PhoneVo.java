package com.ligg.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long phoneId;//id
    private Integer projectId;
    private Long phoneNumber;//手机号
    private LocalDateTime registrationTime;//注册时间
    private String regionName;//地区名称
    private List<ProjectInfoVo> projects;//项目列表
    private String adminNickName;//卡商昵称
    private String adminAvatar;
    private Boolean isAvailable = true;
}
