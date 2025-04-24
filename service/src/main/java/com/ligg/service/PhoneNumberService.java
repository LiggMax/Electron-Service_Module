package com.ligg.service;

import com.ligg.common.entity.Phone;

import java.util.List;
import java.util.Map;

public interface PhoneNumberService {

    /**
     * 条件查询卡号列表
     * @param countryCode 号码归属地
     * @param usageStatus 号码状态
     * @param keyword     搜索关键词
     */
    List<Phone> phoneList(String countryCode, Integer usageStatus, String keyword);

    /**
     * 根据手机号ID查询手机号详情
     *
     * @param phoneId 手机号ID
     * @return 手机号详情（包含基本信息和项目列表）
     */
    Map<String, Object> phoneDetail(Integer phoneId);
}
