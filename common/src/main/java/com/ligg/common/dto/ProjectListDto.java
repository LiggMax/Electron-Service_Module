package com.ligg.common.dto;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 项目列表DTO
 */
@Data
public class ProjectListDto {
    /**
     * 项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
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
