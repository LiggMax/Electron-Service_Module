package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.vo.PageVo;
import com.ligg.common.vo.PhoneVo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PhoneNumberMapper extends BaseMapper<PhoneEntity> {

    /**
     * 条件查询卡号数据
     */
    List<PhoneVo> phoneList(Long adminUserId, Integer regionId, Integer projectId, String keyword);

    /**
     * 批量插入手机号
     *
     * @param phones 手机号列表
     * @return 插入成功的条数
     */
    int batchInsertPhones(List<PhoneEntity> phones);

    /**
     * 根据手机号查询手机实体
     *
     * @param phoneNumber 手机号
     * @return 手机实体
     */
    PhoneEntity getPhoneByNumber(@Param("phoneNumber") Long phoneNumber);

    /**
     * 检查手机号是否已存在
     *
     * @param phoneNumber 手机号
     * @return 存在返回1，不存在返回0
     */
    int checkPhoneExists(@Param("phoneNumber") Long phoneNumber);

    /**
     * 检查手机号和项目的关联是否已存在
     *
     * @param phoneNumber 手机号
     * @param projectId   项目ID
     * @return 存在返回1，不存在返回0
     */
    int checkPhoneProjectRelationExists(@Param("phoneNumber") Long phoneNumber, @Param("projectId") Long projectId);

    /**
     * 插入手机号与项目关联
     *
     * @param phoneNumber 手机号
     * @param projectId   项目ID
     * @param regionId    地区ID
     * @return 插入结果
     */
    int insertPhoneProject(@Param("phoneNumber") Long phoneNumber, @Param("projectId") Long projectId, @Param("regionId") Integer regionId);

    /**
     * 根据项目id获取号码列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("regionId") Integer regionId);

    /**
     * 根据手机号查询关联的项目
     *
     * @param phoneNumber 手机号
     * @return 项目列表
     */
    List<ProjectEntity> getProjectByPhoneNumber(@Param("phoneNumber") Long phoneNumber);

    /**
     * 获取所有手机号列表
     */
    Page<PhoneVo> getPhoneList(@Param("page") IPage<PhoneVo> page,
                               @Param("phoneNumber") Long phoneNumber);

    /**
     * 根据号码统计 is_available = 0 的数量
     */
    int countUnavailablePhones(Long phoneId);

    /**
     * 获取指定地区和项目的可用号码列表
     */
    List<PhoneEntity> getAvailablePhonesByProject(@Param("regionId") Integer regionId,
                                                  @Param("projectId") Integer projectId);
}

