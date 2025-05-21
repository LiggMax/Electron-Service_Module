package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.InvitationRelationsEntity;
import com.ligg.mapper.InvitationRelationsMapper;
import com.ligg.service.common.InvitationRelationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationRelationsServiceImpl extends ServiceImpl<InvitationRelationsMapper, InvitationRelationsEntity> implements InvitationRelationsService {

    @Autowired
    private InvitationRelationsMapper invitationRelationsMapper;

    /**
     * 添加邀请码
     */
    @Override
    public void addInvitationRelations(String invitationCode, String inviterAccount, String inviteeAccount) {
        InvitationRelationsEntity invitationRelations = new InvitationRelationsEntity();
        invitationRelations.setInvitationCode(invitationCode);
        invitationRelations.setInviterAccount(inviterAccount);
        invitationRelations.setInviteeAccount(inviteeAccount);
        invitationRelationsMapper.insert(invitationRelations);
    }
}
