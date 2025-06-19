package com.ligg.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.vo.PageVo;
import com.ligg.common.vo.PhoneVo;

import java.util.List;
import java.util.Map;

public interface PhoneNumberService extends IService<PhoneEntity> {

    /**
     * 条件查询卡号数据
     */
    List<PhoneVo> phoneList(Long adminUserId, Integer regionId, Integer projectId, String keyword);


    /**
     * 根据手机号查询详情
     *
     * @param phoneNumber 手机号
     * @param adminUserId 管理员用户ID
     * @return 详情数据
     */
    PhoneAndProjectDto phoneDetailByNumber(Long phoneNumber, Long adminUserId);


    /**
     * 批量处理手机号和项目关联（新逻辑）
     * 如果号码已存在，只添加项目关联；如果号码不存在，先添加号码再添加关联
     *
     * @param phoneNumbers 手机号列表
     * @param regionId     地区ID
     * @param projectIds   项目ID列表
     * @param adminUserId  管理员用户ID
     * @return 处理结果统计
     */
    Map<String, Integer> batchProcessPhoneAndProjects(List<String> phoneNumbers, Integer regionId, List<Long> projectIds, Long adminUserId);

    /**
     * 检查手机号和项目的关联是否已存在
     *
     * @param phoneNumber 手机号
     * @param projectId   项目ID
     * @return 是否已存在关联
     */
    boolean checkPhoneProjectRelationExists(Long phoneNumber, Long projectId);

    /**
     * 从请求数据中提取地区ID
     *
     * @param uploadData 上传数据
     * @return 地区ID，如果无法识别则返回默认值1
     */
    Integer extractRegionId(Map<String, Object> uploadData);

    /**
     * 从请求数据中提取项目ID列表
     *
     * @param uploadData 上传数据
     * @return 项目ID列表，如果无法识别则返回包含默认值[1]的列表
     */
    List<Long> extractProjectIds(Map<String, Object> uploadData);

    /**
     * 从请求数据中提取所有手机号
     *
     * @param uploadData 上传数据
     * @return 所有有效的手机号列表
     */
    List<String> extractPhoneNumbers(Map<String, Object> uploadData);

    /**
     * 构建新的上传结果数据
     */
    Map<String, Object> buildUploadResultData(int totalProcessed, int totalAdded, int totalExisting, int totalRelationAdded, int totalInvalid);

    /**
     * 将各种类型转换为Integer
     */
    Integer convertToInteger(Object obj, Integer defaultValue);

    /**
     * 将各种类型转换为Long
     */
    Long convertToLong(Object obj, Long defaultValue);

    /**
     * 将国家/地区名称映射为地区ID
     *
     * @param country 国家/地区名称
     * @return 地区ID
     */
    Integer mapCountryToRegionId(String country);

    // 获取手机号列表
    PageVo<PhoneVo> getPhoneList(Long phoneNumber, Long pageNum, Long pageSize);

    //删除手机号
    void deleteBatchByIds(List<Long> phoneIds);
}
