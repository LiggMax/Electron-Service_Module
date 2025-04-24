package com.ligg.service.impl;

import com.ligg.common.dto.PhoneDetailDTO;
import com.ligg.common.entity.Phone;
import com.ligg.mapper.PhoneNumberMapper;
import com.ligg.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    @Autowired
    private PhoneNumberMapper phoneNumberMapper;

    @Override
    public List<Phone> phoneList(String countryCode, Integer usageStatus, String keyword) {
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
        List<PhoneDetailDTO> phoneDetails = phoneNumberMapper.queryByIdPhoneDetail(phoneId);

        // 构建结果
        Map<String, Object> resultMap = new HashMap<>();

        if (phoneDetails != null && !phoneDetails.isEmpty()) {
            // 获取第一条记录的基本信息
            PhoneDetailDTO baseInfo = phoneDetails.get(0);

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
            for (PhoneDetailDTO detail : phoneDetails) {
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
}
