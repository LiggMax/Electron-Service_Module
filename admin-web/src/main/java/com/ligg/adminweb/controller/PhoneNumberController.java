package com.ligg.adminweb.controller;

import com.ligg.common.entity.Phone;
import com.ligg.common.utils.Result;
import com.ligg.service.PhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/phone")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * 条件查询卡号数据
     */
    @GetMapping("/list")
    public Result<List<Phone>> phoneList(
            @RequestParam(required = false) String keyword ,//关键字
            @RequestParam(required = false) String countryCode,//号码归属地
            @RequestParam(required = false) Integer usageStatus //号码状态
            ){
        List<Phone> phoneList = phoneNumberService.phoneList(countryCode, usageStatus, keyword);
        return  Result.success(200,phoneList);
    }

    /**
     * 查询手机号详情
     */
    @GetMapping("/phoneDetail")
    public Result<?> phoneDetail(Integer phoneId){
        // 调用服务获取手机号详情（包含基本信息和项目列表）
        Map<String, Object> phoneDetailData = phoneNumberService.phoneDetail(phoneId);
        return Result.success(200, phoneDetailData);
    }

    /**
     * 批量上传手机号
     */
    @PostMapping("/upload")
    public Result<?> uploadPhoneNumbers(@RequestBody Map<String, Object> uploadData) {
        try {
            // 从请求体中获取国家和项目信息
            String country = (String) uploadData.get("country");
            List<String> projects = (List<String>) uploadData.get("projects");

            // 获取文件列表
            List<Map<String, Object>> files = (List<Map<String, Object>>) uploadData.get("files");

            // 用于记录处理结果
            int totalProcessed = 0; // 总处理数量
            int totalAdded = 0;     // 成功添加数量
            int totalDuplicate = 0; // 重复数 量
            int totalInvalid = 0;   // 无效数量

            // 处理每个文件中的手机号
            for (Map<String, Object> fileData : files) {
                // 获取手机号列表
                List<String> phoneNumbers = (List<String>) fileData.get("phoneNumbers");

                if (phoneNumbers != null) {
                    totalProcessed += phoneNumbers.size();
                    // 批量添加手机号到数据库，并关联到所有选择的项目
                    int added = phoneNumberService.batchAddPhoneNumbers(phoneNumbers, country, projects);
                    totalAdded += added;

                    // 计算重复和无效数量
                    totalDuplicate += phoneNumbers.size() - added;
                }
            }

            // 构建响应结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("total", totalProcessed);
            resultData.put("added", totalAdded);
            resultData.put("duplicate", totalDuplicate);
            resultData.put("invalid", totalInvalid);
            resultData.put("message", "导入成功：成功添加" + totalAdded + "个手机号，" +
                    (totalDuplicate > 0 ? totalDuplicate + "个重复号码已跳过" : ""));

            // 返回成功结果和详细信息
            return Result.success(200, resultData);
        } catch (Exception e) {
            // 异常处理
            log.error("上传失败: {}", e.getMessage());
            return Result.error(500, "上传失败: " + e.getMessage());
        }
    }
}
