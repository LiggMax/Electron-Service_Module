package com.ligg.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.InvitationRelationsEntity;

public interface InvitationRelationsService extends IService<InvitationRelationsEntity> {
    // 添加邀请码
    void addInvitationRelations(String invitationCode, String inviterAccount, String inviteeAccount);
}
