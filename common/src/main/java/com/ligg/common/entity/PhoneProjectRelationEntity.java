package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("phone_project_relation")
public class PhoneProjectRelationEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long phoneNumber;
    private Long projectId;
}
