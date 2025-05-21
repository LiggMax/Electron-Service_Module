package com.ligg.controller;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.common.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 根据项目ID查询项目下的商品列表
     */
    @GetMapping("/commodity")
    public Result<List<RegionCommodityDto>> getProjectCommodityList(@RequestParam Long projectId) {
        List<RegionCommodityDto> projects = projectService.getProjectCommodityList(projectId);
        return Result.success(200,projects);
    }
}
