package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.entity.PhoneEntity;

import java.util.List;

public interface PhoneNumberService extends IService<PhoneEntity> {

    /**
     * 条件查询卡号数据
     */
    List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword);


    /**
     * 根据手机号查询详情
     *
     * @param phoneNumber 手机号
     * @param adminUserId 管理员用户ID
     * @return 详情数据
     */
    PhoneAndProjectDto phoneDetailByNumber(Long phoneNumber, Long adminUserId);

    /**
     * 批量添加手机号
     * @param phoneNumbers 手机号列表
     * @param regionId 地区ID
     * @param projectIds 项目ID列表
     * @param adminUserId 管理员用户ID
     * @return 成功添加的数量
     */
    int batchAddPhoneNumbers(List<String> phoneNumbers, Integer regionId, List<Long> projectIds, Long adminUserId);

}
