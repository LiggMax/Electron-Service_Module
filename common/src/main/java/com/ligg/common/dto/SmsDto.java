package com.ligg.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsDto {
    private Integer userProjectId;
    private String projectName;
    private Long phoneNumber;
    private LocalDateTime createdAt;
}
