package com.ligg.service.file;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.AppVersionEntity;

/**
 * @Author Ligg
 * @Time 2025/5/30
 **/
public interface AppVersionService extends IService<AppVersionEntity> {
    // 保存版本信息
    void saveVersion(String version, String releaseNotes, String downloadUrl);
}
