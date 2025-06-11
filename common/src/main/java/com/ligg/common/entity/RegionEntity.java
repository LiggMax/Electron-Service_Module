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
@TableName("region")
public class RegionEntity {
    @TableId(type = IdType.AUTO)
    private Integer regionId;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 区域标识
     */
    private String regionMark;

    /**
     * 区域图标
     */
    private String icon;

    /**
     * 创建时间
     */
    private LocalDateTime regionCreatedAt;
}
