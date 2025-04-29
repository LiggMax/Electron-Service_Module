package com.ligg.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {
    private Integer projectId;
    private String projectName;
    private Double projectPrice;// 项目价格
    private LocalDateTime projectCreatedAt;// 创建时间
}
