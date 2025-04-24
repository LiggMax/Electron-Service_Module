package com.ligg.service;

import com.ligg.common.entity.PhoneEntity;

import java.util.List;
import java.util.Map;

public interface PhoneNumberService {

    /**
     * 条件查询卡号列表
     * @param countryCode 号码归属地
     * @param usageStatus 号码状态
     * @param keyword     搜索关键词
     */
    List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword);

    /**
     * 根据手机号ID查询手机号详情
     *
     * @param phoneId 手机号ID
     * @return 手机号详情（包含基本信息和项目列表）
     */
    Map<String, Object> phoneDetail(Integer phoneId);

    /**
     * 批量添加手机号
     * @param phoneNumbers 手机号列表
     * @param country 国家
     * @param projects 项目列表
     * @return 成功添加的数量
     */
    int batchAddPhoneNumbers(List<String> phoneNumbers, String country, List<String> projects);
}
