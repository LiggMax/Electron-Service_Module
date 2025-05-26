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
    private String regionName;
    private String regionMark;
    private LocalDateTime regionCreatedAt;
}
