package com.ligg.mapper;

import com.ligg.common.dto.PhoneDetailDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PhoneNumberMapper {

    /**
     * 条件查询卡号数据
     */
    List<PhoneEntity> phoneList(String countryCode, Integer usageStatus, String keyword);

    /**
     * 根据手机号id查询详情
     */
    List<PhoneDetailDto> queryByIdPhoneDetail(Integer phoneId);

    /**
     * 批量插入手机号
     * @param phones 手机号列表
     * @return 插入成功的条数
     */
    int batchInsertPhones(List<PhoneEntity> phones);

    /**
     * 根据手机号查询手机实体
     * @param phoneNumber 手机号
     * @return 手机实体
     */
    PhoneEntity getPhoneByNumber(@Param("phoneNumber") Long phoneNumber);
    
    /**
     * 检查手机号是否已存在
     * @param phoneNumber 手机号
     * @return 存在返回1，不存在返回0
     */
    int checkPhoneExists(@Param("phoneNumber") Long phoneNumber);

    /**
     * 插入手机号与项目关联（用户订单）
     * @param phoneNumber 手机号
     * @param projectId 项目ID
     * @return 插入结果
     */
    int insertPhoneProject(@Param("phoneNumber") Long phoneNumber, @Param("projectId") Integer projectId);

    /**
     * 根据项目id获取号码列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("regionId") Integer regionId);

    /**
     * 根据手机号id删除手机号
     */
    void deletePhone(Integer phoneId);

    /**
     * 获取地区
     */
    List<PhoneEntity> getRegion();

}

