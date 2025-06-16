package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.RegionEntity;
import com.ligg.mapper.ProjectMapper;
import com.ligg.service.common.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目服务实现类
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public List<PhoneEntity> getPhonesByProject(String[] projectNames) {
        return projectMapper.getPhonesByProject(projectNames);
    }

    @Override
    public List<ProjectListDto> getAllProjectsList() {
        return projectMapper.getAllProjectsList();
    }

    @Override
    public List<RegionCommodityDto> getProjectCommodityList(Long projectId) {
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
     *
     * @param projectName 项目名称
     * @return 项目ID，如果不存在则返回null
     */
    @Override
    public Integer getProjectIdByName(String projectName) {
        return projectMapper.getProjectIdByName(projectName);
    }

    /**
     * 添加项目
     */
    @Override
    public void saveProject(ProjectEntity project) {
        projectMapper.insert(project);
    }


    /**
     * 编辑项目
     */
    @Override
    public void updateProject(ProjectEntity project) {
        projectMapper.update(new LambdaUpdateWrapper<ProjectEntity>()
                .eq(ProjectEntity::getProjectId, project.getProjectId())
                .set(ProjectEntity::getProjectPrice, project.getProjectPrice())
                .set(ProjectEntity::getProjectName, project.getProjectName())
                .set(ProjectEntity::getExpirationTime, project.getExpirationTime())
                .set(ProjectEntity::getProjectUpdateAt, LocalDateTime.now()));
    }

    /**
     * 项目图标上传
     */
    @Override
    public void uploadIcon(Integer projectId, String iconUrl) {
        projectMapper.update(new LambdaUpdateWrapper<ProjectEntity>()
                .eq(ProjectEntity::getProjectId, projectId)
                .set(ProjectEntity::getIcon, iconUrl));
    }
}
