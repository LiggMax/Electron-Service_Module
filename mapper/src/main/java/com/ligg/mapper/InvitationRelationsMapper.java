package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.InvitationRelationsEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvitationRelationsMapper extends BaseMapper<InvitationRelationsEntity> {

//    /**
//     * 根据邀请码查询用户信息
//     */
//    UserEntity selectByUserId(String invitationCode);
}
