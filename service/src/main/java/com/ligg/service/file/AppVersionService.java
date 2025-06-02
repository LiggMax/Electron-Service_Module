package com.ligg.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.entity.version.AppVersion;

import java.time.LocalDateTime;
import java.util.List;
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
     * 获取版本列表
     */
    Map<String, Object> getVersionList(Integer page, Integer size);

    /**
     * 检查版本更新
     */
    List<AppVersion> getAppVersionList(String version);
}
