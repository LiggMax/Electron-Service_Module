package com.ligg.service.adminweb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.PhoneProjectRelationEntity;

public interface PhoneProjectRelationService extends IService<PhoneProjectRelationEntity> {

    //保存号码和项目id
    boolean savePhoneProjectRelation(Long phoneNumber, Long projectId);
    
    //直接使用手机号码保存关联关系
    boolean savePhoneNumberProjectRelation(Long phoneNumber, Long projectId);
}
