package com.ligg.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.dto.ChunkUploadDto;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.vo.ChunkUploadVo;
import com.ligg.mapper.AppVersionMapper;
import com.ligg.service.file.AppVersionService;
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
    private record CustomMultipartFile(String name, String originalFilename, String contentType,
                                       byte[] content) implements MultipartFile {

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
    public void saveVersion(String version, String releaseNotes, String downloadUrl, Long fileSize, LocalDateTime updateTime) {
        AppVersionEntity appVersion = new AppVersionEntity();
        appVersion.setVersion(version);
        appVersion.setDownloadUrl(downloadUrl);
        appVersion.setReleaseNotes(releaseNotes);
        appVersion.setFileSize(fileSize);
        appVersion.setUpdateTime(updateTime);
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
            // 合并文件并返回MultipartFile
            MultipartFile mergedFile = mergeChunksToMultipartFile(identifier, progress);
            // 返回需要上传的文件信息，让Controller层处理实际上传
            ChunkUploadVo result = ChunkUploadVo.completed(identifier, null);
            // 将合并后的文件信息传递给Controller
            result.setMergedFile(mergedFile);
            result.setVersion(progress.getVersion());
            result.setReleaseNotes(progress.getReleaseNotes());
            result.setTotalSize(progress.getTotalSize());

            return result;
        }

        return ChunkUploadVo.success(chunkNumber, progress.getUploadedChunks().size(),
                totalChunks, identifier);
    }

    /**
     * 合并分片文件并返回MultipartFile
     */
    private MultipartFile mergeChunksToMultipartFile(String identifier, ChunkUploadProgress progress) throws Exception {
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

        // 验证文件完整性
        if (validateMergedFile(mergedFile, progress.getTotalSize())) {
            // 将合并后的文件转换为MultipartFile
            return createMultipartFileFromPath(mergedFile, originalFilename);
        } else {
            throw new Exception("文件合并后大小不匹配");
        }
    }

    /**
     * 从文件路径创建MultipartFile
     */
    private MultipartFile createMultipartFileFromPath(Path filePath, String originalFilename) throws Exception {
        byte[] content = Files.readAllBytes(filePath);
        return new CustomMultipartFile(
                "file",
                originalFilename,
                "application/octet-stream",
                content
        );
    }

    /**
     * 完成分片上传后的清理工作
     * 此方法应在Controller层调用uploadApp成功后调用
     */
    @Override
    public void completeChunkUpload(String identifier, String downloadUrl) {
        ChunkUploadProgress progress = uploadProgressMap.get(identifier);
        if (progress != null) {

            // 清理临时文件和进度信息
            cleanupTempFiles(identifier);
            uploadProgressMap.remove(identifier);

            log.info("分片上传完成，清理资源: identifier={}", identifier);
        }
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
     * 验证合并后的文件
     */
    private boolean validateMergedFile(Path mergedFile, Long expectedSize) throws Exception {
        long actualSize = Files.size(mergedFile);
        return actualSize == expectedSize;
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
        LambdaQueryWrapper<AppVersionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AppVersionEntity::getUpdateTime);

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
