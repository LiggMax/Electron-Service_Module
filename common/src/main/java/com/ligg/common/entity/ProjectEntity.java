package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("project")
public class ProjectEntity {
    @TableId(value = "project_id", type = IdType.AUTO)
    private Integer projectId;
    private String projectName;
    private Double projectPrice;// 项目价格
    private LocalDateTime projectCreatedAt;// 创建时间
}
