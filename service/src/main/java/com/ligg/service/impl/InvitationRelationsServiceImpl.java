package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.InvitationRelationsEntity;
import com.ligg.common.entity.UserEntity;
import com.ligg.mapper.InvitationRelationsMapper;
import com.ligg.mapper.UserMapper;
import com.ligg.service.InvitationRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationRelationsServiceImpl extends ServiceImpl<InvitationRelationsMapper, InvitationRelationsEntity> implements InvitationRelationsService {

    @Autowired
    private InvitationRelationsMapper invitationRelationsMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 添加邀请码
     */
    @Override
    public void addInvitationRelations(String invitationCode, Long userId) {
        //获取邀请人id
        UserEntity inviterId = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getInvitationCode, invitationCode)
                .select(UserEntity::getUserId));

        InvitationRelationsEntity invitationRelations = new InvitationRelationsEntity();
        invitationRelations.setInvitationCode(invitationCode);
        invitationRelations.setInviterId(userId);
        invitationRelations.setInviterId(inviterId.getUserId());
        invitationRelationsMapper.insert(invitationRelations);
    }
}
