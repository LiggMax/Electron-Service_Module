package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("phone_project_relation")
public class PhoneProjectRelationEntity {
    @TableId
    private Long id;
    private Integer phoneId;
    private Integer projectId;
}
