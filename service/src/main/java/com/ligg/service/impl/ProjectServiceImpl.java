package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.RegionEntity;
import com.ligg.mapper.ProjectMapper;
import com.ligg.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目服务实现类
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper,ProjectEntity> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<ProjectListDto> getAllProjects() {
        return projectMapper.getAllProjects();
    }

    @Override
    public List<PhoneEntity> getPhonesByProject(String[] projectNames) {
        return projectMapper.getPhonesByProject(projectNames);
    }

    @Override
    public List<ProjectListDto> getAllProjectsList() {
        return projectMapper.getAllProjectsList();
    }

    @Override
    public List<RegionCommodityDto> getProjectCommodityList(Integer projectId) {

        return projectMapper.getProjectCommodityList(projectId);
    }

    /**
     * 获取地区和项目列表
     */
    @Override
    public Map<String, Object> getProjectAndRegionList() {
        List<ProjectEntity> project = projectMapper.getProject();

        List<RegionEntity> region = projectMapper.getRegion();

        HashMap<String, Object> map = new HashMap<>();
        map.put("project", project);
        map.put("region", region);
        return map;
    }
    
    /**
     * 根据项目名称查询项目ID
     * @param projectName 项目名称
     * @return 项目ID，如果不存在则返回null
     */
    @Override
    public Integer getProjectIdByName(String projectName) {
        return projectMapper.getProjectIdByName(projectName);
    }
    
    /**
     * 批量根据项目名称查询项目ID
     * @param projectNames 项目名称列表
     * @return 项目ID列表
     */
    @Override
    public List<Integer> getProjectIdsByNames(List<String> projectNames) {
        if (projectNames == null || projectNames.isEmpty()) {
            return new ArrayList<>();
        }
        return projectMapper.getProjectIdsByNames(projectNames);
    }

    /**
     * 添加项目
     */
    @Override
    public void saveProject(String projectName) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setProjectName(projectName);
        projectEntity.setProjectCreatedAt(LocalDateTime.now());
        projectMapper.insert(projectEntity);
    }
}
