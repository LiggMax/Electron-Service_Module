package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.PhoneProjectRelationEntity;
import com.ligg.mapper.PhoneProjectRelationMapper;
import com.ligg.service.adminweb.PhoneProjectRelationService;
import org.springframework.stereotype.Service;

@Service
public class PhoneProjectRelationImpl extends ServiceImpl<PhoneProjectRelationMapper, PhoneProjectRelationEntity> implements PhoneProjectRelationService {
}
