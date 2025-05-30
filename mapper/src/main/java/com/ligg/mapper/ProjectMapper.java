package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.RegionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectMapper extends BaseMapper<ProjectEntity> {


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
    List<RegionCommodityDto> getProjectCommodityList(Long projectId);

    /**
     * 获取所有项目数据
     */
    List<ProjectEntity> getProject();

    /**
     * 获取所哟地区数据
     */
    List<RegionEntity> getRegion();
    
    /**
     * 根据项目名称查询项目ID
     * @param projectName 项目名称
     * @return 项目ID，如果不存在则返回null
     */
    Integer getProjectIdByName(@Param("projectName") String projectName);
    
    /**
     * 批量根据项目名称查询项目ID列表
     * @param projectNames 项目名称列表
     * @return 项目ID列表
     */
    List<Integer> getProjectIdsByNames(@Param("projectNames") List<String> projectNames);

    /**
     * 根据项目名称查询项目id
     */
    ProjectEntity selectByProjectName(String projectName);
}
