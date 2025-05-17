package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.dto.PhoneAndProjectDto;
import com.ligg.common.dto.PhoneDetailDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface PhoneNumberMapper extends BaseMapper<PhoneEntity> {

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
     * 插入手机号与项目关联
     * @param phoneNumber 手机号
     * @param projectId 项目ID
     * @return 插入结果
     */
    int insertPhoneProject(@Param("phoneNumber") Long phoneNumber, @Param("projectId") Long projectId);

    /**
     * 根据项目id获取号码列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("regionId") Integer regionId);

    /**
     * 根据手机号id删除手机号
     */
    void deletePhone(Long phoneId);

    //查询号码详情
    PhoneDetailDto getPhoneDetail(Long phoneId);


    /**
     * 根据手机号查询关联的项目
     * @param phoneNumber 手机号
     * @return 项目列表
     */
    List<ProjectEntity> getProjectByPhoneNumber(@Param("phoneNumber") Long phoneNumber);
    

    /**
     * 获取手机号和关联项目信息（使用DTO对象）
     * @param phoneId 手机号ID
     * @return PhoneAndProjectDto对象，包含嵌套的项目信息
     */
    PhoneAndProjectDto getPhoneAndProject(Long phoneId,Long adminUserId);

    /**
     * 根据地区ID获取所有可用的手机号
     * @param regionId 地区ID
     * @return 可用的手机号列表
     */
    List<PhoneEntity> getAvailablePhonesByRegion(@Param("regionId") Integer regionId);
    
    /**
     * 更新手机号的状态
     * @param phoneId 手机号ID
     * @param status 新状态 (1-可用，0-不可用)
     * @return 受影响的行数
     */
    int updatePhoneStatus(@Param("phoneId") Long phoneId, @Param("status") Integer status);

    /**
     * 删除手机号与项目的关联关系
     * @param phoneId 手机号ID
     * @param projectId 项目ID
     * @return 受影响的行数
     */
    int deletePhoneProjectRelation(@Param("phoneId") Long phoneId, @Param("projectId") Integer projectId);
}

