package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("project")
public class ProjectEntity {
    @TableId(type = IdType.AUTO)
    private Integer projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 过期时间
     */
    @Min(value = 15, message = "过期时间不能小于15分钟")
    private Integer expirationTime;

    /**
     * 项目价格
     */
    private Float projectPrice;

    /**
     * 项目图标
     */
    private String icon;

    /**
     * 创建时间
     */
    private LocalDateTime projectCreatedAt;

    /**
     * 更新时间
     */
    private LocalDateTime projectUpdateAt;
}
