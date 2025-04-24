package com.ligg.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PhoneDetailDTO {
    private Integer phoneId;
    private Long phoneNumber;
    private String countryCode;
    private Integer lineStatus;
    private String usageStatus;
    private LocalDateTime registrationTime;
    // 项目相关字段
    private String projectName;
    private LocalDateTime timeOfUse;

}