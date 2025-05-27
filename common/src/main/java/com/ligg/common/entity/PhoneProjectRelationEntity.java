package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("phone_project_relation")
public class PhoneProjectRelationEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long phoneId;
    private Long projectId;
    private LocalDateTime createdAt;
    private Integer regionId; // 地区ID
    private Integer isAvailable; // 0 = 未使用  1 = 被购买
}
