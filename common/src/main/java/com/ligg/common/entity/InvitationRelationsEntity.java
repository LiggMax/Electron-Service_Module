package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 邀请码关系实体类
 */
@Data
@TableName("invitation_relations")
public class InvitationRelationsEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String inviterAccount; //邀请人账号
    private String inviteeAccount; //被邀请人账号
    private String invitationCode; //使用的邀请码
}
