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

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 手机号码
     */
    private Long phoneNumber;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 地区名称
     */
    private String regionName;

    /**
     * 项目图标
     */
    private String projectIcon;

    /**
     * 地区图标
     */
    private String regionIcon;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
