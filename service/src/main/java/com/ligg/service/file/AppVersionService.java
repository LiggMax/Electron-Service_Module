package com.ligg.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.ChunkUploadDto;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.vo.ChunkUploadVo;

import java.util.Map;

/**
 * @Author Ligg
 * @Time 2025/5/30
 **/
public interface AppVersionService extends IService<AppVersionEntity> {
    // 保存版本信息
    void saveVersion(String version, String releaseNotes, String downloadUrl);

    /**
     * 保存版本信息（包含文件大小）
     */
    void saveVersion(String version, String releaseNotes, String downloadUrl, Long fileSize);

    /**
     * 处理分片上传
     */
    ChunkUploadVo handleChunkUpload(ChunkUploadDto uploadDto) throws Exception;

    /**
     * 获取上传进度
     */
    ChunkUploadVo getUploadProgress(String identifier);

    /**
     * 取消上传
     */
    void cancelUpload(String identifier);

    /**
     * 获取版本列表
     */
    Map<String, Object> getVersionList(Integer page, Integer size);
}
