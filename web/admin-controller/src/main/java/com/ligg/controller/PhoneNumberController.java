package com.ligg.controller;

import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.PhoneNumberService;
import com.ligg.service.ProjectService;
import com.ligg.service.adminweb.PhoneProjectRelationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/phone")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private PhoneProjectRelationService phoneProjectRelationService;

    /**
     * 获取项目和地区数据
     */
    @GetMapping("/projectsAndRegion")
    public Result<Map<String, Object>> getProjectsAndRegion() {
        try {
            Map<String, Object> data = projectService.getProjectAndRegionList();
            return Result.success(200, data);
        } catch (Exception e) {
            log.error("获取项目和地区数据失败: {}", e.getMessage(), e);
            return Result.error(500, "获取项目和地区数据失败: " + e.getMessage());
        }
    }

    /**
     * 条件查询卡号数据
     */
    @GetMapping("/list")
    public Result<List<PhoneEntity>> phoneList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Integer usageStatus) {
        try {
            List<PhoneEntity> phoneList = phoneNumberService.phoneList(countryCode, usageStatus, keyword);
            return Result.success(200, phoneList);
        } catch (Exception e) {
            log.error("查询卡号数据失败: {}", e.getMessage(), e);
            return Result.error(500, "查询卡号数据失败: " + e.getMessage());
        }
    }

    /**
     * 查询手机号详情
     */
    @GetMapping("/phoneDetail")
    public Result<PhoneAndProjectDto> phoneDetail(Long phoneId) {
        if (phoneId == null) {
            log.info("查询号码详情缺少Id");
            return Result.error(400, "请求参数错误");
        }
        Map<String, Object> userInfo = jwtUtil.parseToken(request.getHeader("Token"));
        if (userInfo == null || userInfo.get("userId") == null) {
            log.info("查询号码详情缺少用户id");
            return Result.error(400, "请求参数错误");
        }
        Long adminUserId = (Long) userInfo.get("userId");
        
        try {
            // 1. 获取手机号基本信息
            PhoneEntity phoneEntity = phoneNumberService.getById(phoneId);
            if (phoneEntity == null) {
                log.warn("未找到ID为{}的手机号", phoneId);
                return Result.error(404, "未找到手机号");
            }
            
            // 2. 使用手机号查询详情和关联项目
            PhoneAndProjectDto phoneDetailData = phoneNumberService.phoneDetailByNumber(phoneEntity.getPhoneNumber(), adminUserId);
            
            // 3. 填充手机号ID等基本信息
            if (phoneDetailData == null) {
                phoneDetailData = new PhoneAndProjectDto();
            }
            phoneDetailData.setPhoneId(phoneEntity.getPhoneId());
            phoneDetailData.setPhoneNumber(phoneEntity.getPhoneNumber());
            phoneDetailData.setLineStatus(phoneEntity.getLineStatus());
            phoneDetailData.setUsageStatus(phoneEntity.getUsageStatus());

            return Result.success(200, phoneDetailData);
        } catch (Exception e) {
            log.error("查询手机号详情失败: phoneId={}, error={}", phoneId, e.getMessage(), e);
            return Result.error(500, "查询手机号详情失败");
        }
    }

    /**
     * 批量上传手机号
     */
    @PostMapping("/upload")
    public Result<?> uploadPhoneNumbers(@RequestBody Map<String, Object> uploadData) {
        log.info("接收到上传数据: {}", uploadData);

        // 用于记录处理结果
        int totalProcessed = 0; // 总处理数量
        int totalAdded = 0;     // 成功添加数量
        int totalDuplicate = 0; // 重复数量
        int totalInvalid = 0;   // 无效数量

        try {
            // 获取管理员ID
            Map<String, Object> token = jwtUtil.parseToken(request.getHeader("Token"));
            Long adminUserId = (Long) token.get("userId");
            if (adminUserId == null) {
                return Result.error(401, "未授权的操作，请先登录");
            }
            
            // 1. 提取并验证地区ID
            Integer regionId = extractRegionId(uploadData);
            log.info("提取的地区ID: {}", regionId);

            // 2. 提取并验证项目ID
            List<Integer> projectIds = extractProjectIds(uploadData);
            log.info("提取的项目IDs: {}", projectIds);
            if (CollectionUtils.isEmpty(projectIds)) {
                return Result.error(400, "无法识别有效的项目ID");
            }

            // 3. 解析文件内容并获取手机号列表
            List<String> allPhoneNumbers = extractPhoneNumbers(uploadData);
            if (CollectionUtils.isEmpty(allPhoneNumbers)) {
                return Result.error(400, "没有找到有效的手机号数据");
            }

            totalProcessed = allPhoneNumbers.size();
            log.info("处理手机号: 数量={}, 地区ID={}, 项目IDs={}, 管理员ID={}", 
                    totalProcessed, regionId, projectIds, adminUserId);

            // 4. 批量添加手机号到数据库
            totalAdded = phoneNumberService.batchAddPhoneNumbers(allPhoneNumbers, regionId, projectIds, adminUserId);
            
            // 5. 建立手机号和项目的关联关系（直接使用手机号和项目ID）
            if (totalAdded > 0 && !projectIds.isEmpty()) {
                log.info("开始建立手机号和项目的关联关系, 有效手机号数量: {}, 项目数量: {}", totalAdded, projectIds.size());
                
                // 处理每个有效的手机号
                for (String phoneStr : allPhoneNumbers) {
                    try {
                        // 解析手机号
                        Long phoneNumber = Long.parseLong(phoneStr.trim().replaceAll("[\\s-]", ""));
                        
                        // 为每个项目建立关联
                        for (Integer projectId : projectIds) {
                            try {
                                phoneProjectRelationService.savePhoneNumberProjectRelation(phoneNumber, Long.valueOf(projectId));
                                log.debug("成功建立关联: 手机号={}, 项目ID={}", phoneNumber, projectId);
                            } catch (Exception e) {
                                log.warn("建立手机号与项目关联失败: phoneNumber={}, projectId={}, error={}", 
                                        phoneNumber, projectId, e.getMessage());
                            }
                        }
                    } catch (NumberFormatException e) {
                        log.warn("手机号格式错误，无法建立关联: {}", phoneStr);
                    }
                }
                log.info("手机号和项目关联关系建立完成");
            }

            // 6. 计算重复和无效数量
            totalDuplicate = totalProcessed - totalAdded;

            // 7. 构建响应结果
            Map<String, Object> resultData = buildResultData(totalProcessed, totalAdded, totalDuplicate, totalInvalid);

            // 返回成功结果和详细信息
            return Result.success(200, resultData);
        } catch (Exception e) {
            // 异常处理
            log.error("上传失败: {}", e.getMessage(), e);
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 从请求数据中提取地区ID
     *
     * @param uploadData 上传数据
     * @return 地区ID，如果无法识别则返回默认值1
     */
    private Integer extractRegionId(Map<String, Object> uploadData) {
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
    private List<Integer> extractProjectIds(Map<String, Object> uploadData) {
        List<Integer> projectIds = new ArrayList<>();
        Integer defaultProjectId = 1;

        // 尝试获取项目ID列表 - 兼容多种可能的字段名
        if (uploadData.containsKey("projectIds") && uploadData.get("projectIds") != null) {
            // 新版本使用projectIds，预期是整数列表
            try {
                Object projectIdsObj = uploadData.get("projectIds");
                if (projectIdsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<?> projectIdsList = (List<?>) projectIdsObj;

                    // 处理可能的不同类型（整数或字符串）
                    for (Object idObj : projectIdsList) {
                        Integer projectId = convertToInteger(idObj, null);
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
                if (projectsObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<?> projectsList = (List<?>) projectsObj;
                    List<Integer> mappedIds = mapProjectNamesToIds(projectsList);
                    projectIds.addAll(mappedIds);
                    log.info("从projects字段映射项目ID列表: {}", projectIds);
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
        List<Integer> validProjectIds = new ArrayList<>();
        for (Integer id : projectIds) {
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
    private List<String> extractPhoneNumbers(Map<String, Object> uploadData) {
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
    private Map<String, Object> buildResultData(int totalProcessed, int totalAdded, int totalDuplicate, int totalInvalid) {
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
    private Integer convertToInteger(Object obj, Integer defaultValue) {
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
     * 将国家/地区名称映射为地区ID
     *
     * @param country 国家/地区名称
     * @return 地区ID
     */
    private Integer mapCountryToRegionId(String country) {
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

        // 如果数据库查询失败，使用静态映射作为回退
        if (regionId == null) {
            Map<String, Integer> countryMap = new HashMap<>();
            countryMap.put("中国", 1);
            countryMap.put("美国", 2);
            countryMap.put("英国", 3);
            countryMap.put("日本", 4);
            countryMap.put("韩国", 5);

            regionId = countryMap.getOrDefault(country.trim(), null);
        }

        return regionId;
    }

    /**
     * 将项目名称列表映射为项目ID列表
     *
     * @param projectObjects 项目对象列表
     * @return 项目ID列表
     */
    private List<Integer> mapProjectNamesToIds(List<?> projectObjects) {
        List<Integer> projectIds = new ArrayList<>();

        if (projectObjects == null || projectObjects.isEmpty()) {
            return projectIds;
        }

        for (Object project : projectObjects) {
            // 直接的字符串列表
            if (project instanceof String) {
                String projectName = (String) project;
                if (projectName != null && !projectName.trim().isEmpty()) {
                    Integer projectId = projectService.getProjectIdByName(projectName.trim());
                    if (projectId != null && projectId > 0) {
                        projectIds.add(projectId);
                    }
                }
            }
            // Map格式的项目对象
            else if (project instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> projectMap = (Map<String, Object>) project;

                // 尝试直接获取projectId
                Object projectIdObj = projectMap.get("projectId");
                if (projectIdObj != null) {
                    Integer projectId = convertToInteger(projectIdObj, null);
                    if (projectId != null && projectId > 0) {
                        projectIds.add(projectId);
                    }
                }
                // 如果没有projectId，尝试通过projectName查询
                else {
                    Object projectNameObj = projectMap.get("projectName");
                    if (projectNameObj instanceof String) {
                        String projectName = (String) projectNameObj;
                        if (projectName != null && !projectName.trim().isEmpty()) {
                            Integer projectId = projectService.getProjectIdByName(projectName.trim());
                            if (projectId != null && projectId > 0) {
                                projectIds.add(projectId);
                            }
                        }
                    }
                }
            }
        }

        return projectIds;
    }
}
