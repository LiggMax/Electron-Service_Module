package com.ligg.common.dto;

import lombok.Data;

/**
 * 项目列表DTO
 */
@Data
public class ProjectListDto {
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 项目名称
     */
    private String projectName;
    
    /**
     * 项目价格
     */
    private Double projectPrice;
    
    /**
     * 项目手机号数量
     */
    private Integer phoneCount;
}
