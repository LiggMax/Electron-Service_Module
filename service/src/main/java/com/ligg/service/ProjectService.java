package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 项目服务接口
 */
public interface ProjectService extends IService<ProjectEntity> {
    
    /**
     * 获取所有项目列表及数量
     * @return 项目列表
     */
    List<ProjectListDto> getAllProjects();
    
    /**
     * 根据项目名称查询手机号列表
     * @param projectNames 项目名称数组
     * @return 手机号列表
     */
    List<PhoneEntity> getPhonesByProject(String[] projectNames);

    /**
     * 获取所有项目列表
     */
    List<ProjectListDto> getAllProjectsList();

    /**
     * 获取指定项目商品列表
     */
    List<RegionCommodityDto> getProjectCommodityList(Integer projectId);

    /**
     * 获取项目和地区列表
     */
    Map<String, Object> getProjectAndRegionList();
    
    /**
     * 根据项目名称查询项目ID
     * @param projectName 项目名称
     * @return 项目ID，如果不存在则返回null
     */
    Integer getProjectIdByName(String projectName);
    
    /**
     * 批量根据项目名称查询项目ID
     * @param projectNames 项目名称列表
     * @return 项目ID列表
     */
    List<Integer> getProjectIdsByNames(List<String> projectNames);

    // 新增项目
    void saveProject(String projectName);
}
