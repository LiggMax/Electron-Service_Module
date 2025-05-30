package com.ligg.common.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分片上传DTO
 */
@Data
public class ChunkUploadDto {

    /**
     * 文件分片
     */
    private MultipartFile chunk;

    /**
     * 当前分片序号（从0开始）
     */
    private Integer chunkNumber;

    /**
     * 分片总数
     */
    private Integer totalChunks;

    /**
     * 当前分片大小
     */
    private Long chunkSize;

    /**
     * 文件总大小
     */
    private Long totalSize;

    /**
     * 文件唯一标识（MD5）
     */
    private String identifier;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件相对路径
     */
    private String relativePath;

    /**
     * 版本号
     */
    private String version;

    /**
     * 发布说明
     */
    private String releaseNotes;
} 