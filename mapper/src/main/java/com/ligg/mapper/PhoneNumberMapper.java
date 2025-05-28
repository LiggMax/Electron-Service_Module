package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.vo.PhoneVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PhoneNumberMapper extends BaseMapper<PhoneEntity> {

    /**
     * 条件查询卡号数据
     */
    List<PhoneVo> phoneList(Long adminUserId,String countryCode, String keyword);

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
     * @param regionId 地区ID
     * @return 插入结果
     */
    int insertPhoneProject(@Param("phoneNumber") Long phoneNumber, @Param("projectId") Long projectId, @Param("regionId") Integer regionId);

    /**
     * 根据项目id获取号码列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("regionId") Integer regionId);

    /**
     * 根据手机号查询关联的项目
     * @param phoneNumber 手机号
     * @return 项目列表
     */
    List<ProjectEntity> getProjectByPhoneNumber(@Param("phoneNumber") Long phoneNumber);
    
    /**
     * 获取所有手机号列表
     */
    List<PhoneVo> getPhoneList();
}

