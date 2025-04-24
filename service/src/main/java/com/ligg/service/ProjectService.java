package com.ligg.service;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.entity.PhoneEntity;

import java.util.List;

/**
 * 项目服务接口
 */
public interface ProjectService {
    
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
}
