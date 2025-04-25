package com.ligg.mapper;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProjectMapper {


    /**
     * 获取所有项目列表及数量
     * @return 项目列表
     */
    List<ProjectListDto> getAllProjects();

    /**
     * 根据项目名称查询项目数量
     */
    Integer getProjectCountByName(String projectName);

    /**
     * 根据项目名称查询手机号列表
     * @param projectNames 项目名称数组
     * @return 手机号列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("projectNames") String[] projectNames);

    /**
     * 获取所有项目列表
     */
    List<ProjectListDto> getAllProjectsList();

    /**
     * 获取指定项目商品列表
     */
    List<RegionCommodityDto> getProjectCommodityList(Integer projectId);

}
