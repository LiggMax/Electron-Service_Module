package com.ligg.service.impl;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.mapper.ProjectMapper;
import com.ligg.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目服务实现类
 */
@Service
public class ProjectServiceImpl implements ProjectService {

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
}
