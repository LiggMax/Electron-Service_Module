package com.ligg.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.ChunkUploadDto;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.vo.ChunkUploadVo;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author Ligg
 * @Time 2025/5/30
 **/
public interface AppVersionService extends IService<AppVersionEntity> {

    /**
     * 保存版本信息
     */
    void saveVersion(String version, String releaseNotes, String downloadUrl, Long fileSize, LocalDateTime updateTime);

    /**
     * 处理分片上传
     */
    ChunkUploadVo handleChunkUpload(ChunkUploadDto uploadDto) throws Exception;

    /**
     * 完成分片上传后的清理工作
     * 此方法应在Controller层调用uploadApp成功后调用
     */
    void completeChunkUpload(String identifier);

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
