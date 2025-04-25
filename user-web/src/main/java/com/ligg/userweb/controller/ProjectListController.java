package com.ligg.userweb.controller;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目列表控制器
 */
@RestController
@RequestMapping("/api/project")
public class ProjectListController {

    @Autowired
    private ProjectService projectService;

    /**
     * 获取所有项目列表
     */
    @GetMapping("/list")
    public Result<List<ProjectListDto>> getAllProjects() {
        List<ProjectListDto> projects = projectService.getAllProjectsList();
        return Result.success(200,projects);
    }
    
    /**
     * 根据项目名称查询手机号列表
     * @param projectName 项目名称
     * @return 手机号列表
     */
    @GetMapping("/phones/{projectName}")
    public Result<List<PhoneEntity>> getPhonesByProject(@PathVariable String projectName) {
        List<PhoneEntity> phones = projectService.getPhonesByProject(new String[]{projectName});
        return Result.success(200,phones);
    }
}
