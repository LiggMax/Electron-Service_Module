package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
     * 项目价格
     */
    private Float projectPrice;

    /**
     * 项目图标
     */
    private String icon;

    //TODO 关键词和验证码长度需要删除
    /**
     * 解析关键字
     */
    @NotNull
    private String keyword;

    /**
     * 验证码长度
     */
    @Min(1)
    @Max(20)
    private int codeLength;

    /**
     * 创建时间
     */
    private LocalDateTime projectCreatedAt;

    /**
     * 更新时间
     */
    private LocalDateTime projectUpdateAt;
}
