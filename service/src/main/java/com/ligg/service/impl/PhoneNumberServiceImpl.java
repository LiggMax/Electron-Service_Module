package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.PhoneDetailDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.service.PhoneNumberService;
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
import java.util.stream.Collectors;

@Slf4j
@Service
public class PhoneNumberServiceImpl extends ServiceImpl<PhoneNumberMapper,PhoneEntity> implements PhoneNumberService {

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Override
    public List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword) {
        return phoneNumberMapper.phoneList(countryCode, usageStatus, keyword);
    }

    /**
     * 根据手机号ID查询手机号详情
     *
     * @param phoneId 手机号ID
     * @return 手机号详情（包含基本信息和项目列表）
     */
    @Override
    public Map<String, Object> phoneDetail(Integer phoneId) {
        // 获取手机号的详细信息
        List<PhoneDetailDto> phoneDetails = phoneNumberMapper.queryByIdPhoneDetail(phoneId);
        Map<String, Object> resultMap = new HashMap<>();
        
        // 如果没有找到手机号详情，返回空结果
        if (CollectionUtils.isEmpty(phoneDetails)) {
            return resultMap;
        }
        
        // 获取第一条记录的基本信息
        PhoneDetailDto baseInfo = phoneDetails.get(0);
        
        // 构建基本信息
        Map<String, Object> phoneInfo = new HashMap<>();
        phoneInfo.put("phoneId", baseInfo.getPhoneId());
        phoneInfo.put("phoneNumber", baseInfo.getPhoneNumber());
        phoneInfo.put("countryCode", baseInfo.getCountryCode());
        phoneInfo.put("lineStatus", baseInfo.getLineStatus());
        phoneInfo.put("usageStatus", baseInfo.getUsageStatus());
        phoneInfo.put("registrationTime", baseInfo.getRegistrationTime());
        
        // 添加基本信息到结果
        resultMap.put("basicInfo", phoneInfo);
        
        // 构建项目列表
        List<Map<String, Object>> projectList = phoneDetails.stream()
                .filter(detail -> detail.getProjectName() != null)
                .map(detail -> {
                    Map<String, Object> projectMap = new HashMap<>();
                    projectMap.put("projectName", detail.getProjectName());
                    projectMap.put("timeOfUse", detail.getTimeOfUse());
                    return projectMap;
                })
                .collect(Collectors.toList());
        
        // 添加项目列表到结果
        resultMap.put("projects", projectList);
        
        return resultMap;
    }

    /**
     * 批量添加手机号
     *
     * @param phoneNumbers 手机号列表
     * @param regionId 地区ID
     * @param projectIds 项目ID列表
     * @return 成功添加的数量
     */
    @Override
    @Transactional
    public int batchAddPhoneNumbers(List<String> phoneNumbers, Integer regionId, List<Integer> projectIds) {
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
        Integer primaryProjectId = projectIds.get(0);
        
        // 确保primaryProjectId不是默认值
        if (primaryProjectId <= 0) {
            log.warn("项目ID无效，已设置为默认值1: {}", primaryProjectId);
            primaryProjectId = 1;
        }
        
        // 记录实际使用的地区ID和项目ID
        log.info("实际使用的地区ID: {}, 主项目ID: {}", regionId, primaryProjectId);
        
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
            phonesToInsert.add(createPhoneEntity(phoneNumber, regionId, primaryProjectId, now));
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
    private PhoneEntity createPhoneEntity(Long phoneNumber, Integer regionId, Integer projectId, LocalDateTime now) {
        PhoneEntity phone = new PhoneEntity();
        phone.setPhoneNumber(phoneNumber);
        phone.setPhoneRegionId(regionId);
        phone.setPhoneProjectId(projectId);
        phone.setLineStatus(1);
        phone.setUsageStatus(1);
        phone.setRegistrationTime(now);
        return phone;
    }
    
    /**
     * 执行批量插入手机号及关联
     */
    private int insertPhones(List<PhoneEntity> phonesToInsert, List<Long> validPhoneNumbers, List<Integer> projectIds) {
        try {
            // 批量插入手机号
            int totalAdded = phoneNumberMapper.batchInsertPhones(phonesToInsert);
            
            // 为每个项目插入关联信息到user_order表
            for (Long phoneNumber : validPhoneNumbers) {
                for (Integer projectId : projectIds) {
                    phoneNumberMapper.insertPhoneProject(phoneNumber, projectId);
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
