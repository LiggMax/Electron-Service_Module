package com.ligg.controller;

import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
