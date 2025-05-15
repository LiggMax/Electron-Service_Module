package com.ligg.controller;

import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.ProjectService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目
 */
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 项目列表
     */
    @GetMapping
    public Result<List<ProjectEntity>> getProjectList(){
        return Result.success(200,projectService.getBaseMapper().selectList(null));
    }

    /**
     * 添加号码
     */
    @PostMapping("/add")
    public Result<String> addProject(@RequestParam String projectName){
        projectService.saveProject(projectName);
        return Result.success(200,"添加成功");
    }

    /**
     * 删除项目
     */
    @DeleteMapping
    public Result<String> deleteProject(@RequestParam Long projectId){
        projectService.removeById(projectId);
        return Result.success(200,"删除成功");
    }

}
