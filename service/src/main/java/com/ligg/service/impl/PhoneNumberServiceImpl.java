package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.service.PhoneNumberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PhoneNumberServiceImpl extends ServiceImpl<PhoneNumberMapper,PhoneEntity> implements PhoneNumberService {

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    HttpServletRequest request;

    @Override
    public List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword) {
        return phoneNumberMapper.phoneList(countryCode, usageStatus, keyword);
    }


    /**
     * 根据手机号查询详情
     *
     * @param phoneNumber 手机号
     * @param adminUserId 管理员用户ID
     * @return 详情数据
     */
    @Override
    public PhoneAndProjectDto phoneDetailByNumber(Long phoneNumber, Long adminUserId) {
        try {
            // 创建基础DTO对象
            PhoneAndProjectDto dto = new PhoneAndProjectDto();
            
            // 获取手机号基本信息
            PhoneEntity phoneEntity = phoneNumberMapper.getPhoneByNumber(phoneNumber);
            if (phoneEntity == null) {
                log.warn("未找到手机号: {}", phoneNumber);
                return null;
            }
            
            // 设置基本信息
            dto.setPhoneNumber(phoneNumber);
            dto.setLineStatus(phoneEntity.getLineStatus());
            dto.setUsageStatus(phoneEntity.getUsageStatus());
            dto.setRegistrationTime(phoneEntity.getRegistrationTime());
            
            // 获取地区信息
            Integer regionId = phoneEntity.getPhoneRegionId();
            if (regionId != null) {
                // 这里可以添加获取地区信息的逻辑
                // 例如: RegionEntity region = regionMapper.selectById(regionId);
                // dto.setRegionName(region.getRegionName());
                // dto.setCountryCode(region.getRegionCode());
            }
            
            // 获取项目列表
            List<ProjectEntity> projects = phoneNumberMapper.getProjectByPhoneNumber(phoneNumber);
            dto.setProjects(projects);
            
            return dto;
        } catch (Exception e) {
            log.error("根据手机号查询详情失败: phoneNumber={}, error={}", phoneNumber, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 批量添加手机号
     *
     * @param phoneNumbers 手机号列表
     * @param regionId 地区ID
     * @param projectIds 项目ID列表
     * @param adminUserId 管理员用户ID
     * @return 成功添加的数量
     */
    @Override
    @Transactional
    public int batchAddPhoneNumbers(List<String> phoneNumbers, Integer regionId, List<Long> projectIds, Long adminUserId) {
        // 参数校验
        if (CollectionUtils.isEmpty(phoneNumbers) || CollectionUtils.isEmpty(projectIds) || regionId == null) {
            log.warn("批量添加手机号参数无效: phoneNumbers={}, regionId={}, projectIds={}", 
                    phoneNumbers, regionId, projectIds);
            return 0;
        }
        
        // 确保regionId不是默认值
        if (regionId <= 0) {
            log.warn("地区ID无效，已设置为默认值1: {}", regionId);
            regionId = 1;
        }
        
        // 获取第一个项目ID作为主项目ID
        Long primaryProjectId = projectIds.get(0);
        
        // 确保primaryProjectId不是默认值
        if (primaryProjectId <= 0) {
            log.warn("项目ID无效，已设置为默认值1: {}", primaryProjectId);
            primaryProjectId = 1L;
        }
        
        // 记录实际使用的地区ID和项目ID
        log.info("实际使用的地区ID: {}, 主项目ID: {}, 管理员ID: {}", regionId, primaryProjectId, adminUserId);
        
        LocalDateTime now = LocalDateTime.now();
        
        // 筛选有效的手机号
        List<Long> validPhoneNumbers = new ArrayList<>();
        List<PhoneEntity> phonesToInsert = new ArrayList<>();
        
        // 处理每个手机号
        for (String phoneStr : phoneNumbers) {
            // 解析手机号
            Long phoneNumber = parsePhoneNumber(phoneStr);
            if (phoneNumber == null) {
                continue;
            }
            
            // 检查是否存在
            if (isPhoneExist(phoneNumber)) {
                continue;
            }
            
            // 添加到有效列表
            validPhoneNumbers.add(phoneNumber);
            phonesToInsert.add(createPhoneEntity(phoneNumber, regionId,now, adminUserId));
        }
        
        // 如果没有有效的手机号，直接返回
        if (phonesToInsert.isEmpty()) {
            log.info("没有有效的手机号可添加");
            return 0;
        }
        
        // 执行插入操作
        return insertPhones(phonesToInsert, validPhoneNumbers, projectIds);
    }
    
    /**
     * 解析手机号字符串为Long类型
     */
    private Long parsePhoneNumber(String phoneStr) {
        if (phoneStr == null || phoneStr.trim().isEmpty()) {
            log.warn("手机号为空");
            return null;
        }
        
        // 清除可能的空格、破折号等字符
        String cleanPhoneStr = phoneStr.trim().replaceAll("[\\s-]", "");
        
        try {
            return Long.parseLong(cleanPhoneStr);
        } catch (NumberFormatException e) {
            log.warn("手机号格式错误: {}", phoneStr);
            return null;
        }
    }
    
    /**
     * 检查手机号是否已存在
     */
    private boolean isPhoneExist(Long phoneNumber) {
        return phoneNumberMapper.checkPhoneExists(phoneNumber) > 0;
    }
    
    /**
     * 创建手机号实体对象
     */
    private PhoneEntity createPhoneEntity(Long phoneNumber, Integer regionId, LocalDateTime now, Long adminUserId) {
        PhoneEntity phone = new PhoneEntity();
        phone.setPhoneNumber(phoneNumber);
        phone.setPhoneRegionId(regionId);
        phone.setLineStatus(1);
        phone.setUsageStatus(1);
        phone.setRegistrationTime(now);
        phone.setAdminUserId(adminUserId);
        return phone;
    }
    
    /**
     * 执行批量插入手机号及关联
     */
    private int insertPhones(List<PhoneEntity> phonesToInsert, List<Long> validPhoneNumbers, List<Long> projectIds) {
        try {
            // 批量插入手机号
            int totalAdded = phoneNumberMapper.batchInsertPhones(phonesToInsert);
            
            // 为每个项目插入关联信息到 phone_project_relation 表
            for (Long phoneNumber : validPhoneNumbers) {
                for (Long projectId : projectIds) {
                    try {
                        phoneNumberMapper.insertPhoneProject(phoneNumber, projectId);
                    } catch (Exception e) {
                        log.error("插入手机号和项目关联失败: phoneNumber={}, projectId={}, error={}", 
                                phoneNumber, projectId, e.getMessage());
                    }
                }
            }
            
            log.info("成功添加手机号: {}, 关联项目: {}", totalAdded, projectIds);
            return totalAdded;
        } catch (Exception e) {
            log.error("批量添加手机号失败: {}", e.getMessage(), e);
            throw e; // 抛出异常以触发事务回滚
        }
    }
}
