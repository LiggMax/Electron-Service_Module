package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.common.ProjectService;
import com.ligg.service.file.FileService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 项目管理
 */
@RestController
@RequestMapping("/api/admin_web/project")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FileService fileService;

    /**
     * 项目列表
     */
    @GetMapping
    public Result<List<ProjectEntity>> getProjectList() {
        return Result.success(200, projectService.getBaseMapper()
                .selectList(new LambdaQueryWrapper<ProjectEntity>()
                        .orderByDesc(ProjectEntity::getProjectUpdateAt)));
    }

    /**
     * 添加项目
     */
    @PostMapping("/add")
    public Result<String> addProject(@RequestBody ProjectEntity projectEntity) {
        projectService.saveProject(projectEntity);
        return Result.success(200, "添加成功");
    }

    /**
     * 修改项目
     */
    @PutMapping("/edit")
    public Result<String> editProject(@RequestBody ProjectEntity projectEntity) {
        projectService.updateProject(projectEntity);
        return Result.success(200, "修改成功");
    }

    /**
     * 删除项目
     */
    @DeleteMapping
    public Result<String> deleteProject(@RequestParam Integer projectId) {
        projectService.removeById(projectId);
        return Result.success(200, "删除成功");
    }

    /**
     * 项目图标上传
     */
    @PostMapping("/upload_icon")
    public Result<String> uploadIcon(@RequestParam("projectId") @NotNull Integer projectId,
                                     @RequestParam("icon") MultipartFile icon) {
        if (icon.getSize() > 10 * 1024 * 1024) {
            return Result.error(400, "文件大小不合法");
        }
        String iconUrl = fileService.uploadImage(icon);
        projectService.uploadIcon(projectId, iconUrl);
        return Result.success(200, "上传成功");
    }
}
