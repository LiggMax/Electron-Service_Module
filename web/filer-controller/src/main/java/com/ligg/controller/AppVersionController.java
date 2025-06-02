package com.ligg.controller;

import com.ligg.common.annotation.RequireAuth;
import com.ligg.common.entity.version.AppVersion;
import com.ligg.common.utils.Result;
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
     * 检查版本更新（无需权限验证）
     */
    @GetMapping("/check")
    @RequireAuth(required = false)
    public Result<List<AppVersion>> checkVersion(@RequestParam(required = false) String version) {
        try {
            List<AppVersion> result = appVersionService.getAppVersionList(version);
            return Result.success(200, result);
        } catch (Exception e) {
            return Result.error(500, "检查版本失败");
        }
    }

    /**
     * 版本上传接口
     */
    @PostMapping("/upload")
    public Result<String> uploadVersion(@RequestPart("appFile") MultipartFile appFile,
                                        @RequestPart("version") String version,
                                        @RequestPart("releaseNotes") @Pattern(regexp = "^.{1,100}$") String releaseNotes) {
        // 文件大小检查（传统上传限制为500MB）
        if (appFile.getSize() > 200 * 1024 * 1024) {
            return Result.error(400, "文件过大");
        }

        long startTime = System.currentTimeMillis();
        try {
            String downloadUrl = fileService.uploadApp(appFile);
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("文件上传完成: 大小={}, 耗时={}ms", appFile.getSize(), duration);

            if (downloadUrl != null) {
                // 保存版本信息
                appVersionService.saveVersion(version, releaseNotes, downloadUrl, appFile.getSize(), LocalDateTime.now());
                return Result.success(200, "上传成功");
            }
            return Result.error(500, "上传失败");
        } catch (Exception e) {
            log.error("文件上传失败: error={}", e.getMessage(), e);
            return Result.error(500, "上传失败: " + e.getMessage());
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
            return Result.success(200, result);
        } catch (Exception e) {
            log.error("获取版本列表失败: error={}", e.getMessage());
            return Result.error(500, "获取版本列表失败");
        }
    }
}
