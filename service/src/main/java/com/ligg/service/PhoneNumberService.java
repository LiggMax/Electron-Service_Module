package com.ligg.service;

import com.ligg.common.entity.PhoneEntity;

import java.util.List;
import java.util.Map;

public interface PhoneNumberService {

    /**
     * 条件查询卡号数据
     */
    List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword);

    /**
     * 查询手机号详情
     * @param phoneId 手机号ID
     * @return 详情数据
     */
    Map<String, Object> phoneDetail(Integer phoneId);

    /**
     * 批量添加手机号
     * @param phoneNumbers 手机号列表
     * @param regionId 地区ID
     * @param projectIds 项目ID列表
     * @return 成功添加的数量
     */
    int batchAddPhoneNumbers(List<String> phoneNumbers, Integer regionId, List<Integer> projectIds);
}
