package com.ligg.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.ChunkUploadDto;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.vo.ChunkUploadVo;
import com.ligg.mapper.AppVersionMapper;
import com.ligg.service.file.AppVersionService;
import com.ligg.service.file.FileService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 版本管理服务实现类
 */
@Slf4j
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersionEntity> implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Autowired
    private FileService fileService;

    @Value("${file.upload.temp-path:./temp}")
    private String tempPath;

    // 存储分片上传进度信息
    private final ConcurrentHashMap<String, ChunkUploadProgress> uploadProgressMap = new ConcurrentHashMap<>();

    /**
     * 分片上传进度信息
     */
    @Getter
    @Setter
    private static class ChunkUploadProgress {
        private final Set<Integer> uploadedChunks = ConcurrentHashMap.newKeySet();
        private Integer totalChunks;
        private String filename;
        private String version;
        private String releaseNotes;
        private Long totalSize;
        private String finalDownloadUrl;
        private final LocalDateTime createTime = LocalDateTime.now();
    }

    /**
     * 自定义的MultipartFile实现类
     */
    private static class CustomMultipartFile implements MultipartFile {
        private final String name;
        private final String originalFilename;
        private final String contentType;
        private final byte[] content;

        public CustomMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
            this.name = name;
            this.originalFilename = originalFilename;
            this.contentType = contentType;
            this.content = content;
        }

        @NotNull
        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalFilename() {
            return originalFilename;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @NotNull
        @Override
        public byte[] getBytes() {
            return content;
        }

        @NotNull
        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(content);
        }

        @Override
        public void transferTo(@NotNull File dest) throws IOException {
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                fos.write(content);
            }
        }
    }

    /**
     * 保存版本信息
     */
    @Override
    public void saveVersion(String version, String releaseNotes, String downloadUrl) {
        AppVersionEntity appVersion = new AppVersionEntity();
        appVersion.setVersion(version);
        appVersion.setDownloadUrl(downloadUrl);
        appVersion.setReleaseNotes(releaseNotes);
        appVersion.setUpdateTime(LocalDateTime.now());
        appVersionMapper.insert(appVersion);
    }

    /**
     * 保存版本信息（包含文件大小）
     */
    @Override
    public void saveVersion(String version, String releaseNotes, String downloadUrl, Long fileSize) {
        AppVersionEntity appVersion = new AppVersionEntity();
        appVersion.setVersion(version);
        appVersion.setDownloadUrl(downloadUrl);
        appVersion.setReleaseNotes(releaseNotes);
        appVersion.setUpdateTime(LocalDateTime.now());
        appVersionMapper.insert(appVersion);
    }

    /**
     * 处理分片上传
     */
    @Override
    public ChunkUploadVo handleChunkUpload(ChunkUploadDto uploadDto) throws Exception {
        String identifier = uploadDto.getIdentifier();
        Integer chunkNumber = uploadDto.getChunkNumber();
        Integer totalChunks = uploadDto.getTotalChunks();

        // 获取或创建进度信息
        ChunkUploadProgress progress = uploadProgressMap.computeIfAbsent(identifier, k -> {
            ChunkUploadProgress p = new ChunkUploadProgress();
            p.setTotalChunks(totalChunks);
            p.setFilename(uploadDto.getFilename());
            p.setVersion(uploadDto.getVersion());
            p.setReleaseNotes(uploadDto.getReleaseNotes());
            p.setTotalSize(uploadDto.getTotalSize());
            return p;
        });

        // 保存分片文件
        String chunkFileName = saveChunkFile(uploadDto);
        progress.getUploadedChunks().add(chunkNumber);
        log.info("分片保存成功: {}/{}, 文件: {}", chunkNumber + 1, totalChunks, chunkFileName);

        // 检查是否所有分片都已上传完成
        if (progress.getUploadedChunks().size() == totalChunks) {
            // 合并文件
            String finalDownloadUrl = mergeChunks(identifier, progress);
            if (finalDownloadUrl != null) {
                progress.setFinalDownloadUrl(finalDownloadUrl);

                // 保存版本信息到数据库
                if (progress.getVersion() != null && progress.getReleaseNotes() != null) {
                    saveVersion(progress.getVersion(), progress.getReleaseNotes(),
                            finalDownloadUrl, progress.getTotalSize());
                }

                // 清理临时文件和进度信息
                cleanupTempFiles(identifier);
                uploadProgressMap.remove(identifier);

                return ChunkUploadVo.completed(identifier, finalDownloadUrl);
            }
        }

        return ChunkUploadVo.success(chunkNumber, progress.getUploadedChunks().size(),
                totalChunks, identifier);
    }

    /**
     * 保存分片文件
     */
    private String saveChunkFile(ChunkUploadDto uploadDto) throws Exception {
        String identifier = uploadDto.getIdentifier();
        Integer chunkNumber = uploadDto.getChunkNumber();

        // 创建临时目录
        Path tempDir = Paths.get(tempPath, identifier);
        Files.createDirectories(tempDir);

        // 分片文件名
        String chunkFileName = "chunk_" + chunkNumber;
        Path chunkFilePath = tempDir.resolve(chunkFileName);

        // 保存分片
        try (InputStream inputStream = uploadDto.getChunk().getInputStream();
             FileOutputStream outputStream = new FileOutputStream(chunkFilePath.toFile())) {

            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        }

        return chunkFileName;
    }

    /**
     * 合并分片文件
     */
    private String mergeChunks(String identifier, ChunkUploadProgress progress) throws Exception {
        Path tempDir = Paths.get(tempPath, identifier);
        String originalFilename = progress.getFilename();

        // 创建合并后的临时文件
        Path mergedFile = tempDir.resolve(originalFilename);

        try (FileOutputStream mergedStream = new FileOutputStream(mergedFile.toFile())) {
            // 按顺序合并分片
            for (int i = 0; i < progress.getTotalChunks(); i++) {
                Path chunkFile = tempDir.resolve("chunk_" + i);
                if (Files.exists(chunkFile)) {
                    try (FileInputStream chunkStream = new FileInputStream(chunkFile.toFile())) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = chunkStream.read(buffer)) != -1) {
                            mergedStream.write(buffer, 0, bytesRead);
                        }
                    }
                    // 删除已合并的分片
                    Files.deleteIfExists(chunkFile);
                } else {
                    throw new Exception("分片文件不存在: chunk_" + i);
                }
            }
            mergedStream.flush();
        }

        // 验证文件完整性（可选）
        if (validateMergedFile(mergedFile, progress.getTotalSize())) {
            // 上传到最终存储位置
            return uploadMergedFile(mergedFile.toFile(), originalFilename);
        } else {
            throw new Exception("文件合并后大小不匹配");
        }
    }

    /**
     * 验证合并后的文件
     */
    private boolean validateMergedFile(Path mergedFile, Long expectedSize) throws Exception {
        long actualSize = Files.size(mergedFile);
        return actualSize == expectedSize;
    }

    /**
     * 上传合并后的文件
     */
    private String uploadMergedFile(File mergedFile, String originalFilename) throws Exception {
        // 将File转换为MultipartFile，然后调用现有的uploadApp方法
        try (FileInputStream inputStream = new FileInputStream(mergedFile)) {
            // 创建CustomMultipartFile
            byte[] content = Files.readAllBytes(mergedFile.toPath());
            CustomMultipartFile multipartFile = new CustomMultipartFile(
                    "file",
                    originalFilename,
                    "application/octet-stream",
                    content
            );

            // 调用现有的uploadApp方法
            return fileService.uploadApp(multipartFile);
        } catch (Exception e) {
            log.error("上传合并文件失败: " + e.getMessage(), e);
            throw new Exception("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 清理临时文件
     */
    private void cleanupTempFiles(String identifier) {
        try {
            Path tempDir = Paths.get(tempPath, identifier);
            if (Files.exists(tempDir)) {
                Files.walk(tempDir)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (Exception e) {
            log.warn("清理临时文件失败: identifier=" + identifier + ", error=" + e.getMessage());
        }
    }

    /**
     * 获取上传进度
     */
    @Override
    public ChunkUploadVo getUploadProgress(String identifier) {
        ChunkUploadProgress progress = uploadProgressMap.get(identifier);
        if (progress == null) {
            return ChunkUploadVo.error("未找到上传进度信息");
        }

        int uploadedCount = progress.getUploadedChunks().size();
        int totalCount = progress.getTotalChunks();
        boolean completed = uploadedCount == totalCount && progress.getFinalDownloadUrl() != null;

        if (completed) {
            return ChunkUploadVo.completed(identifier, progress.getFinalDownloadUrl());
        } else {
            return ChunkUploadVo.success(null, uploadedCount, totalCount, identifier);
        }
    }

    /**
     * 取消上传
     */
    @Override
    public void cancelUpload(String identifier) {
        // 清理临时文件
        cleanupTempFiles(identifier);
        // 移除进度信息
        uploadProgressMap.remove(identifier);
        log.info("取消上传并清理资源: identifier=" + identifier);
    }

    /**
     * 获取版本列表
     */
    @Override
    public Map<String, Object> getVersionList(Integer page, Integer size) {
        Page<AppVersionEntity> pageInfo = new Page<>(page, size);
        QueryWrapper<AppVersionEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");

        Page<AppVersionEntity> result = appVersionMapper.selectPage(pageInfo, queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("pages", result.getPages());

        return data;
    }
}
