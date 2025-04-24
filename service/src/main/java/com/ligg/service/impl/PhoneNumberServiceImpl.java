package com.ligg.service.impl;

import com.ligg.common.dto.PhoneDetailDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

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

        // 构建结果
        Map<String, Object> resultMap = new HashMap<>();

        if (phoneDetails != null && !phoneDetails.isEmpty()) {
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
            List<Map<String, Object>> projectList = new ArrayList<>();
            for (PhoneDetailDto detail : phoneDetails) {
                if (detail.getProjectName() != null) {
                    Map<String, Object> projectMap = new HashMap<>();
                    projectMap.put("projectName", detail.getProjectName());
                    projectMap.put("timeOfUse", detail.getTimeOfUse());
                    projectList.add(projectMap);
                }
            }

            // 添加项目列表到结果
            resultMap.put("projects", projectList);
        }

        return resultMap;
    }

    /**
     * 批量添加手机号
     *
     * @param phoneNumbers 手机号列表
     * @param country 国家
     * @param projects 项目列表
     * @return 成功添加的数量
     */
    @Override
    public int batchAddPhoneNumbers(List<String> phoneNumbers, String country, List<String> projects) {
        if (phoneNumbers == null || phoneNumbers.isEmpty() || projects == null || projects.isEmpty()) {
            return 0;
        }
        // 创建Phone对象列表
        List<PhoneEntity> phones = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // 转换手机号并创建Phone对象
        for (String phoneStr : phoneNumbers) {
            try {
                // 将字符串转换为Long类型的手机号
                Long phoneNumber = Long.parseLong(phoneStr);

                // 创建新的Phone对象
                PhoneEntity phone = new PhoneEntity();
                phone.setPhoneNumber(phoneNumber);
                phone.setCountryCode(country);      // 设置国家
                phone.setLineStatus(1);             // 默认在线状态
                phone.setUsageStatus("已使用");            // 默认使用状态为正常
                phone.setRegistrationTime(now);     // 设置注册时间

                phones.add(phone);
            } catch (NumberFormatException e) {
                // 忽略无法转换为数字的手机号
                continue;
            }
        }

        // 如果没有有效的手机号需要添加，则直接返回0
        if (phones.isEmpty()) {
            return 0;
        }
        // 批量插入手机号，使用INSERT IGNORE语法处理唯一键冲突
        int result = phoneNumberMapper.batchInsertPhones(phones);

        // 插入项目关联信息
        for (PhoneEntity phone : phones) {
            for (String project : projects) {
                phoneNumberMapper.insertPhoneProject(phone.getPhoneNumber(), project);
            }
        }
        return result;
    }
}
