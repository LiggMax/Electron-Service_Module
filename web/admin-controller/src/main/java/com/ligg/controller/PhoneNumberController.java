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
            Integer regionId = phoneNumberService.extractRegionId(uploadData);
            log.info("提取的地区ID: {}", regionId);

            // 2. 提取并验证项目ID
            List<Long> projectIds = phoneNumberService.extractProjectIds(uploadData);
            log.info("提取的项目IDs: {}", projectIds);
            if (CollectionUtils.isEmpty(projectIds)) {
                return Result.error(400, "无法识别有效的项目ID");
            }

            // 3. 解析文件内容并获取手机号列表
            List<String> allPhoneNumbers = phoneNumberService.extractPhoneNumbers(uploadData);
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
                        Long phoneNumber = phoneNumberService.convertToLong(phoneStr.trim().replaceAll("[\\s-]", ""), null);
                        if (phoneNumber == null) continue;
                        
                        // 为每个项目建立关联
                        for (Long projectId : projectIds) {
                            try {
                                phoneProjectRelationService.savePhoneNumberProjectRelation(phoneNumber, projectId);
                                log.debug("成功建立关联: 手机号={}, 项目ID={}", phoneNumber, projectId);
                            } catch (Exception e) {
                                log.warn("建立手机号与项目关联失败: phoneNumber={}, projectId={}, error={}", 
                                        phoneNumber, projectId, e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        log.warn("手机号格式错误，无法建立关联: {}", phoneStr);
                    }
                }
                log.info("手机号和项目关联关系建立完成");
            }

            // 6. 计算重复和无效数量
            totalDuplicate = totalProcessed - totalAdded;

            // 7. 构建响应结果
            Map<String, Object> resultData = phoneNumberService.buildResultData(totalProcessed, totalAdded, totalDuplicate, totalInvalid);

            // 返回成功结果和详细信息
            return Result.success(200, resultData);
        } catch (Exception e) {
            // 异常处理
            log.error("上传失败: {}", e.getMessage(), e);
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }
}
