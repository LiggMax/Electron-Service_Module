package com.ligg.controller;

import com.ligg.common.annotation.RequireAuth;
import com.ligg.common.dto.ChunkUploadDto;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.ChunkUploadVo;
import com.ligg.service.file.AppVersionService;
import com.ligg.service.file.FileService;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
     * 分片上传接口
     */
    @PostMapping("/chunk-upload")
    public Result<ChunkUploadVo> chunkUpload(
            @RequestParam("chunk") MultipartFile chunk,
            @RequestParam("chunkNumber") Integer chunkNumber,
            @RequestParam("totalChunks") Integer totalChunks,
            @RequestParam("chunkSize") Long chunkSize,
            @RequestParam("totalSize") Long totalSize,
            @RequestParam("identifier") String identifier,
            @RequestParam("filename") String filename,
            @RequestParam(value = "version", required = false) String version,
            @RequestParam(value = "releaseNotes", required = false) String releaseNotes) {

        // 参数验证
        if (chunk.isEmpty()) {
            return Result.error(400, "分片文件不能为空");
        }

        if (totalSize > (long) 1024 * 1024 * 1024) { // 1GB限制
            return Result.error(400, "文件大小超过限制（最大1GB）");
        }

        try {
            log.info("接收分片上传: 文件={}, 分片={}/{}, 大小={}",
                    filename, chunkNumber + 1, totalChunks, chunk.getSize());

            // 构建分片上传DTO
            ChunkUploadDto uploadDto = new ChunkUploadDto();
            uploadDto.setChunk(chunk);
            uploadDto.setChunkNumber(chunkNumber);
            uploadDto.setTotalChunks(totalChunks);
            uploadDto.setChunkSize(chunkSize);
            uploadDto.setTotalSize(totalSize);
            uploadDto.setIdentifier(identifier);
            uploadDto.setFilename(filename);
            uploadDto.setVersion(version);
            uploadDto.setReleaseNotes(releaseNotes);

            // 处理分片上传
            ChunkUploadVo result = appVersionService.handleChunkUpload(uploadDto);

            // 如果分片上传完成且包含合并后的文件，则上传到MinIO
            if (result.getCompleted() && result.getMergedFile() != null) {
                try {
                    // 调用现有的uploadApp方法上传合并后的文件
                    String downloadUrl = fileService.uploadApp(result.getMergedFile());

                    if (downloadUrl != null) {
                        // 设置下载地址
                        result.setDownloadUrl(downloadUrl);

                        // 完成分片上传的清理工作
                        appVersionService.completeChunkUpload(identifier, downloadUrl);

                        // 清除临时的MultipartFile引用
                        result.setMergedFile(null);
                        result.setVersion(null);
                        result.setReleaseNotes(null);
                        result.setTotalSize(null);

                        log.info("分片上传完成: identifier={}, downloadUrl={}", identifier, downloadUrl);
                    } else {
                        log.error("文件上传失败: identifier={}", identifier);
                        return Result.error(500, "文件上传到存储服务失败");
                    }
                } catch (Exception e) {
                    log.error("上传合并文件失败: identifier={}, error={}", identifier, e.getMessage(), e);
                    return Result.error(500, "上传合并文件失败: " + e.getMessage());
                }
            }

            return Result.success(200, result);
        } catch (Exception e) {
            log.error("分片上传失败: identifier={}, chunkNumber={}, error={}",
                    identifier, chunkNumber, e.getMessage(), e);
            return Result.error(500, "分片上传失败: " + e.getMessage());
        }
    }

    /**
     * 查询上传进度
     */
    @GetMapping("/upload-progress/{identifier}")
    public Result<ChunkUploadVo> getUploadProgress(@PathVariable String identifier) {
        try {
            ChunkUploadVo progress = appVersionService.getUploadProgress(identifier);
            return Result.success(200, progress);
        } catch (Exception e) {
            log.error("查询上传进度失败: identifier={}, error={}", identifier, e.getMessage());
            return Result.error(500, "查询进度失败");
        }
    }

    /**
     * 取消上传
     */
    @DeleteMapping("/cancel-upload/{identifier}")
    public Result<String> cancelUpload(@PathVariable String identifier) {
        try {
            appVersionService.cancelUpload(identifier);
            log.info("取消上传成功: identifier={}", identifier);
            return Result.success(200, "取消上传成功");
        } catch (Exception e) {
            log.error("取消上传失败: identifier={}, error={}", identifier, e.getMessage());
            return Result.error(500, "取消上传失败");
        }
    }

    /**
     * 传统上传接口（小文件）
     */
    @PostMapping("/upload")
    public Result<String> uploadVersion(@RequestPart("appFile") MultipartFile appFile,
                                        @RequestPart("version") String version,
                                        @RequestPart("releaseNotes") @Pattern(regexp = "^.{1,100}$") String releaseNotes) {
        // 文件大小检查（传统上传限制为100MB）
        if (appFile.getSize() > 100 * 1024 * 1024) {
            return Result.error(400, "文件过大，请使用分片上传接口");
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
