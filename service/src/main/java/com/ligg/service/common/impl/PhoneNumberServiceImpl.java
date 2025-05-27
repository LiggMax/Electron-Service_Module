package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.vo.PhoneVo;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.service.common.PhoneNumberService;
import com.ligg.service.common.ProjectService;
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
    private ProjectService projectService;

    /**
     * 获取卡商号码列表
     */
    @Override
    public List<PhoneVo> phoneList(Long adminUserId, String countryCode, Integer usageStatus, String keyword) {
        return phoneNumberMapper.phoneList(adminUserId,countryCode, usageStatus, keyword);
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
                log.warn("未找到手机号: " + phoneNumber);
                return null;
            }
            
            // 设置基本信息
            dto.setPhoneNumber(phoneNumber);
            dto.setRegistrationTime(phoneEntity.getRegistrationTime());
            
            // 获取项目列表
            List<ProjectEntity> projects = phoneNumberMapper.getProjectByPhoneNumber(phoneNumber);
            dto.setProjects(projects);
            
            return dto;
        } catch (Exception e) {
            log.error("根据手机号查询详情失败: phoneNumber=" + phoneNumber + ", error=" + e.getMessage(), e);
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

            //TODO 号码价格暂时默认0.20元
            Float money = 0.20f;
            // 添加到有效列表
            validPhoneNumbers.add(phoneNumber);
            phonesToInsert.add(createPhoneEntity(phoneNumber, now, adminUserId, money));
        }
        
        // 如果没有有效的手机号，直接返回
        if (phonesToInsert.isEmpty()) {
            log.info("没有有效的手机号可添加");
            return 0;
        }

        // 执行插入操作
        return insertPhones(phonesToInsert, validPhoneNumbers, projectIds, regionId);
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
    private PhoneEntity createPhoneEntity(Long phoneNumber, LocalDateTime now, Long adminUserId, Float money) {
        PhoneEntity phone = new PhoneEntity();
        phone.setPhoneNumber(phoneNumber);
        phone.setRegistrationTime(now);
        phone.setAdminUserId(adminUserId);
        phone.setMoney(money);
        return phone;
    }
    
    /**
     * 执行批量插入手机号及关联
     */
    private int insertPhones(List<PhoneEntity> phonesToInsert, List<Long> validPhoneNumbers, List<Long> projectIds, Integer regionId) {
        try {
            // 批量插入手机号
            int totalAdded = phoneNumberMapper.batchInsertPhones(phonesToInsert);
            
            // 为每个项目插入关联信息到 phone_project_relation 表
            for (Long phoneNumber : validPhoneNumbers) {
                for (Long projectId : projectIds) {
                    try {
                    phoneNumberMapper.insertPhoneProject(phoneNumber, projectId, regionId);
                    } catch (Exception e) {
                        log.error("插入手机号和项目关联失败: phoneNumber=" + phoneNumber + ", projectId=" + projectId + ", error=" + e.getMessage());
                    }
                }
            }
            
            log.info("成功添加手机号: " + totalAdded + ", 关联项目: " + projectIds);
            return totalAdded;
        } catch (Exception e) {
            log.error("批量添加手机号失败: " + e.getMessage(), e);
            throw e; // 抛出异常以触发事务回滚
        }
    }
    
    /**
     * 从请求数据中提取地区ID
     *
     * @param uploadData 上传数据
     * @return 地区ID，如果无法识别则返回默认值1
     */
    @Override
    public Integer extractRegionId(Map<String, Object> uploadData) {
        // 默认地区ID
        Integer defaultRegionId = 1;
        Integer resultRegionId = null;

        // 尝试获取地区ID - 兼容多种可能的字段名
        if (uploadData.containsKey("regionId")) {
            // 新版本使用regionId
            Object regionIdObj = uploadData.get("regionId");
            resultRegionId = convertToInteger(regionIdObj, null);
            log.info("从regionId字段提取地区ID: {}", resultRegionId);
        }

        if (resultRegionId == null && uploadData.containsKey("countryId")) {
            // 使用countryId
            Object countryIdObj = uploadData.get("countryId");
            resultRegionId = convertToInteger(countryIdObj, null);
            log.info("从countryId字段提取地区ID: {}", resultRegionId);
        }

        if (resultRegionId == null && uploadData.containsKey("country")) {
            // 旧版本使用country，需要做映射
            String country = (String) uploadData.get("country");
            // 根据country字符串查找对应的regionId
            resultRegionId = mapCountryToRegionId(country);
            log.info("从country字段映射地区ID: {}", resultRegionId);
        }

        // 如果没有找到有效的regionId，使用默认值
        if (resultRegionId == null || resultRegionId <= 0) {
            log.warn("未找到有效的regionId，使用默认值：{}", defaultRegionId);
            resultRegionId = defaultRegionId;
        }

        return resultRegionId;
    }

    /**
     * 从请求数据中提取项目ID列表
     *
     * @param uploadData 上传数据
     * @return 项目ID列表，如果无法识别则返回包含默认值[1]的列表
     */
    @Override
    public List<Long> extractProjectIds(Map<String, Object> uploadData) {
        List<Long> projectIds = new ArrayList<>();
        Long defaultProjectId = 1L;

        // 尝试获取项目ID列表 - 兼容多种可能的字段名
        if (uploadData.containsKey("projectIds") && uploadData.get("projectIds") != null) {
            // 新版本使用projectIds，预期是整数列表
            try {
                Object projectIdsObj = uploadData.get("projectIds");
                if (projectIdsObj instanceof List<?> projectIdsList) {

                    // 处理可能的不同类型（整数或字符串）
                    for (Object idObj : projectIdsList) {
                        Long projectId = convertToLong(idObj, null);
                        if (projectId != null && projectId > 0) {
                            projectIds.add(projectId);
                        }
                    }

                    log.info("从projectIds字段提取项目ID列表: {}", projectIds);
                }
            } catch (Exception e) {
                log.warn("projectIds格式转换失败: {}", e.getMessage());
            }
        } else if (uploadData.containsKey("projects") && uploadData.get("projects") != null) {
            // 处理projects字段，可能是对象数组或字符串数组
            try {
                Object projectsObj = uploadData.get("projects");
                if (projectsObj instanceof List<?> projectsList) {

                    // 直接从每个项目对象中提取 projectId
                    for (Object project : projectsList) {
                        if (project instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> projectMap = (Map<String, Object>) project;
                            
                            // 获取 projectId
                            if (projectMap.containsKey("projectId")) {
                                Object projectIdObj = projectMap.get("projectId");
                                // 直接处理 Long 类型的 projectId
                                Long projectId = convertToLong(projectIdObj, null);
                                if (projectId != null && projectId > 0) {
                                    projectIds.add(projectId);
                                    continue;
                                }
                            }
                            
                            // 如果没有有效的 projectId，尝试通过 projectName 查询
                            Object projectNameObj = projectMap.get("projectName");
                            if (projectNameObj instanceof String projectName) {
                                if (!projectName.trim().isEmpty()) {
                                    // 获取项目ID并转换为Long
                                    Integer projectIdInt = projectService.getProjectIdByName(projectName.trim());
                                    if (projectIdInt != null && projectIdInt > 0) {
                                        projectIds.add(Long.valueOf(projectIdInt));
                                    }
                                }
                            }
                        } else if (project instanceof String projectName) {
                            // 处理字符串类型的项目名称
                            if (!projectName.trim().isEmpty()) {
                                // 获取项目ID并转换为Long
                                Integer projectIdInt = projectService.getProjectIdByName(projectName.trim());
                                if (projectIdInt != null && projectIdInt > 0) {
                                    projectIds.add(Long.valueOf(projectIdInt));
                                }
                            }
                        }
                    }
                    
                    log.info("从projects字段提取项目ID列表: {}", projectIds);
                }
            } catch (Exception e) {
                log.warn("projects格式转换失败: {}", e.getMessage());
            }
        }

        // 如果没有找到有效的项目ID，使用默认值
        if (projectIds.isEmpty()) {
            projectIds.add(defaultProjectId);
            log.warn("未找到有效的项目ID，使用默认值：{}", projectIds);
        }

        // 检查所有项目ID的有效性
        List<Long> validProjectIds = new ArrayList<>();
        for (Long id : projectIds) {
            if (id != null && id > 0) {
                validProjectIds.add(id);
            }
        }

        // 如果过滤后没有有效ID，使用默认值
        if (validProjectIds.isEmpty()) {
            validProjectIds.add(defaultProjectId);
            log.warn("所有项目ID均无效，使用默认值：{}", validProjectIds);
        }

        return validProjectIds;
    }

    /**
     * 从请求数据中提取所有手机号
     *
     * @param uploadData 上传数据
     * @return 所有有效的手机号列表
     */
    @Override
    public List<String> extractPhoneNumbers(Map<String, Object> uploadData) {
        List<String> allPhoneNumbers = new ArrayList<>();

        try {
            // 获取文件列表
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> files = (List<Map<String, Object>>) uploadData.get("files");

            if (files != null && !files.isEmpty()) {
                // 处理每个文件中的手机号
                for (Map<String, Object> fileData : files) {
                    // 获取手机号列表
                    @SuppressWarnings("unchecked")
                    List<String> phoneNumbers = (List<String>) fileData.get("phoneNumbers");

                    if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
                        // 过滤掉空字符串和无效字符
                        List<String> validPhoneNumbers = phoneNumbers.stream()
                                .filter(phone -> phone != null && !phone.trim().isEmpty())
                                .map(phone -> phone.trim().replaceAll("[\\s-]", ""))
                                .toList();

                        allPhoneNumbers.addAll(validPhoneNumbers);
                    }
                }
            }

            log.info("提取的手机号数量: {}", allPhoneNumbers.size());
        } catch (Exception e) {
            log.error("解析手机号数据失败: {}", e.getMessage(), e);
        }

        return allPhoneNumbers;
    }

    /**
     * 构建上传结果数据
     */
    @Override
    public Map<String, Object> buildResultData(int totalProcessed, int totalAdded, int totalDuplicate, int totalInvalid) {
        Map<String, Object> resultData = new HashMap<>();
        resultData.put("total", totalProcessed);
        resultData.put("added", totalAdded);
        resultData.put("duplicate", totalDuplicate);
        resultData.put("invalid", totalInvalid);

        String message = "导入成功：成功添加" + totalAdded + "个手机号";
        if (totalDuplicate > 0) {
            message += "，" + totalDuplicate + "个重复号码已跳过";
        }
        if (totalInvalid > 0) {
            message += "，" + totalInvalid + "个无效号码已忽略";
        }

        resultData.put("message", message);
        return resultData;
    }

    /**
     * 将各种类型转换为Integer
     */
    @Override
    public Integer convertToInteger(Object obj, Integer defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        if (obj instanceof Integer) {
            return (Integer) obj;
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                log.warn("无法解析字符串为整数: {}", obj);
                return defaultValue;
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        return defaultValue;
    }

    /**
     * 将各种类型转换为Long
     */
    @Override
    public Long convertToLong(Object obj, Long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }

        if (obj instanceof Long) {
            return (Long) obj;
        } else if (obj instanceof Integer) {
            return Long.valueOf((Integer) obj);
        } else if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                log.warn("无法解析字符串为长整数: {}", obj);
                return defaultValue;
            }
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        }

        return defaultValue;
    }

    /**
     * 将国家/地区名称映射为地区ID
     *
     * @param country 国家/地区名称
     * @return 地区ID
     */
    @Override
    public Integer mapCountryToRegionId(String country) {
        if (country == null || country.trim().isEmpty()) {
            return null;
        }

        // 尝试通过数据库查询获取地区ID
        Integer regionId = null;
        try {
            // TODO: 实现通过数据库查询获取地区ID的逻辑
            // regionId = regionService.getRegionIdByName(country);
        } catch (Exception e) {
            log.warn("通过数据库查询地区ID失败: {}", e.getMessage());
        }
        return regionId;
    }

    /**
     * 获取号码列表
     */
    @Override
    public List<PhoneVo> getPhoneList() {
        return phoneNumberMapper.getPhoneList();
    }
}
