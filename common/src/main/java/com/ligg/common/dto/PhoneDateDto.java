package com.ligg.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PhoneDateDto {
    private Integer phoneId;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long phoneNumber;
    private String countryCode;
    private Integer lineStatus;
    private String usageStatus;
    private LocalDateTime registrationTime;
    // 项目相关字段
    private String projectName;
    private LocalDateTime timeOfUse;

}