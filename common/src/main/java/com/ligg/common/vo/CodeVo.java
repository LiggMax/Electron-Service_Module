package com.ligg.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CodeVo {
    private Integer id;
    private String code;
    private Long phoneNumber;
    private LocalDateTime createTime;
}
