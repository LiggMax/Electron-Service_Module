package com.ligg.controller;

import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.common.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 项目管理
 */
@RestController
@RequestMapping("/api/adminWeb/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    /**
     * 项目列表
     */
    @GetMapping
    public Result<List<ProjectEntity>> getProjectList() {
        return Result.success(200, projectService.getBaseMapper().selectList(null));
    }

    /**
     * 添加项目
     */
    @PostMapping("/add")
    public Result<String> addProject(@RequestParam String projectName,
                                     @RequestParam Double projectPrice) {
        if(!projectService.nameFindProjectInfo(projectName)){
            return Result.error(400, "项目已存在");
        }
        projectService.saveProject(projectName, projectPrice);
        return Result.success(200, "添加成功");
    }

    /**
     * 修改项目
     */
    @PutMapping("/edit")
    public Result<String> editProject(@RequestParam Long projectId,
                                      @RequestParam Double projectPrice,
                                      @RequestParam(required = false) String projectName) {
        projectService.updateProject(projectId, projectPrice, projectName);
        return Result.success(200, "修改成功");
    }

    /**
     * 删除项目
     */
    @DeleteMapping
    public Result<String> deleteProject(@RequestParam Long projectId) {
        projectService.removeById(projectId);
        return Result.success(200, "删除成功");
    }
}
