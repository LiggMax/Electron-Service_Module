package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.PhoneProjectRelationEntity;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.mapper.PhoneProjectRelationMapper;
import com.ligg.service.adminweb.PhoneProjectRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;

@Slf4j
@Service
public class PhoneProjectRelationImpl extends ServiceImpl<PhoneProjectRelationMapper, PhoneProjectRelationEntity> implements PhoneProjectRelationService {

    @Autowired
    private PhoneProjectRelationMapper phoneProjectRelationMapper;
    
    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    /**
     * 保存手机号和项目关系
     */
    @Override
    public boolean savePhoneProjectRelation(Long phoneNumber, Long projectId) {
        try {
            // 根据手机号查询手机号ID
            PhoneEntity phoneEntity = phoneNumberMapper.getPhoneByNumber(phoneNumber);
            if (phoneEntity == null || phoneEntity.getPhoneId() == null) {
                log.warn("未找到手机号: {}", phoneNumber);
                return false;
            }
            
            // 保存关联关系
            PhoneProjectRelationEntity relationEntity = new PhoneProjectRelationEntity();
            relationEntity.setPhoneId(phoneEntity.getPhoneId());
            relationEntity.setProjectId(projectId);
            relationEntity.setCreatedAt(LocalDateTime.now());
            phoneProjectRelationMapper.insert(relationEntity);
            
            log.info("成功保存手机号和项目关系: 手机号={}, 项目ID={}", phoneNumber, projectId);
            return true;
        } catch (Exception e) {
            log.error("保存手机号和项目关系失败: 手机号={}, 项目ID={}, 错误={}", phoneNumber, projectId, e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * 直接使用手机号码保存关联关系
     * 先查询手机号对应的ID，再保存关联关系
     */
    @Override
    public boolean savePhoneNumberProjectRelation(Long phoneNumber, Long projectId) {
        try {
            // 根据手机号查询手机号ID
            PhoneEntity phoneEntity = phoneNumberMapper.getPhoneByNumber(phoneNumber);
            if (phoneEntity == null || phoneEntity.getPhoneId() == null) {
                log.warn("未找到手机号: {}", phoneNumber);
                return false;
            }
            
            // 保存关联关系
            PhoneProjectRelationEntity relationEntity = new PhoneProjectRelationEntity();
            relationEntity.setPhoneId(phoneEntity.getPhoneId());
            relationEntity.setProjectId(projectId);
            relationEntity.setCreatedAt(LocalDateTime.now());
            phoneProjectRelationMapper.insert(relationEntity);
            
            log.info("成功保存手机号和项目关系: 手机号={}, 项目ID={}", phoneNumber, projectId);
            return true;
        } catch (Exception e) {
            log.error("保存手机号和项目关系失败: 手机号={}, 项目ID={}, 错误={}", phoneNumber, projectId, e.getMessage(), e);
            return false;
        }
    }
}
