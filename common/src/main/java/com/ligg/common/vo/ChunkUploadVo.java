package com.ligg.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分片上传结果VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkUploadVo {

    /**
     * 是否上传成功
     */
    private Boolean success;

    /**
     * 当前分片序号
     */
    private Integer chunkNumber;

    /**
     * 已上传分片数量
     */
    private Integer uploadedChunks;

    /**
     * 总分片数量
     */
    private Integer totalChunks;

    /**
     * 上传进度百分比
     */
    private Double progress;

    /**
     * 是否全部上传完成
     */
    private Boolean completed;

    /**
     * 文件标识
     */
    private String identifier;

    /**
     * 最终文件下载地址（完成后才有）
     */
    private String downloadUrl;

    /**
     * 消息
     */
    private String message;

    /**
     * 创建成功结果
     */
    public static ChunkUploadVo success(Integer chunkNumber, Integer uploadedChunks, Integer totalChunks, String identifier) {
        ChunkUploadVo vo = new ChunkUploadVo();
        vo.setSuccess(true);
        vo.setChunkNumber(chunkNumber);
        vo.setUploadedChunks(uploadedChunks);
        vo.setTotalChunks(totalChunks);
        vo.setProgress((double) uploadedChunks / totalChunks * 100);
        vo.setCompleted(uploadedChunks.equals(totalChunks));
        vo.setIdentifier(identifier);
        vo.setMessage("分片上传成功");
        return vo;
    }

    /**
     * 创建完成结果
     */
    public static ChunkUploadVo completed(String identifier, String downloadUrl) {
        ChunkUploadVo vo = new ChunkUploadVo();
        vo.setSuccess(true);
        vo.setCompleted(true);
        vo.setProgress(100.0);
        vo.setIdentifier(identifier);
        vo.setDownloadUrl(downloadUrl);
        vo.setMessage("文件上传完成");
        return vo;
    }

    /**
     * 创建失败结果
     */
    public static ChunkUploadVo error(String message) {
        ChunkUploadVo vo = new ChunkUploadVo();
        vo.setSuccess(false);
        vo.setCompleted(false);
        vo.setMessage(message);
        return vo;
    }
} 