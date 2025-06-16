package com.ligg.controller;

import com.ligg.common.entity.version.AppVersion;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.Result;
import com.ligg.service.annotation.RequireAuth;
import com.ligg.service.file.AppVersionService;
import com.ligg.service.file.FileService;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 软件更新控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/version")
@RequireAuth("版本管理权限验证")
public class AppVersionController {

    @Autowired
    private FileService fileService;

    @Autowired
    private AppVersionService appVersionService;


    /**
     * 检查版本更新（
     */
    @GetMapping("/check")
    @RequireAuth(required = false)
    public Result<List<AppVersion>> checkVersion(String version, Integer app) {
        try {
            List<AppVersion> result = appVersionService.getAppVersionList(version, app);
            return Result.success(BusinessStates.SUCCESS, result);
        } catch (Exception e) {
            return Result.error(BusinessStates.INTERNAL_SERVER_ERROR, "检查版本失败");
        }
    }

    /**
     * 版本上传接口
     */
    @PostMapping(value = "/upload")
    public Result<String> uploadVersion(@RequestPart("appFile") MultipartFile appFile,
                                        @RequestParam("version") String version,
                                        @RequestParam("releaseNotes") @Pattern(regexp = "^.{1,100}$") String releaseNotes,
                                        Integer app) {
        // 文件大小检查（传统上传限制为BusinessStatus.SUCCESSMB）
        if (appFile.getSize() > 200 * 1024 * 1024) {
            return Result.error(BusinessStates.BAD_REQUEST, "文件过大");
        }
        try {
            String downloadUrl = fileService.uploadApp(appFile, app);
            if (downloadUrl != null) {
                appVersionService.saveVersion(version, releaseNotes, downloadUrl, appFile.getSize(), app, LocalDateTime.now());
                return Result.success(BusinessStates.SUCCESS, "上传成功");
            } else {
                return Result.error(BusinessStates.INTERNAL_SERVER_ERROR, "上传失败");
            }
        } catch (
                Exception e) {
            log.error("文件上传失败: error={}", e.getMessage(), e);
            return Result.error(BusinessStates.INTERNAL_SERVER_ERROR, "上传失败: " + e.getMessage());
        }
    }

    /**
     * 获取版本列表（无需权限验证）
     */
    @GetMapping("/list")
    @RequireAuth(required = false)
    public Result<Map<String, Object>> getVersionList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        try {
            Map<String, Object> result = appVersionService.getVersionList(page, size);
            return Result.success(BusinessStates.SUCCESS, result);
        } catch (Exception e) {
            log.error("获取版本列表失败: error={}", e.getMessage());
            return Result.error(BusinessStates.INTERNAL_SERVER_ERROR, "获取版本列表失败");
        }
    }
}
