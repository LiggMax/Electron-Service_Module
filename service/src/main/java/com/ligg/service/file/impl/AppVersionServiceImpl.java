package com.ligg.service.file.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.mapper.AppVersionMapper;
import com.ligg.service.file.AppVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @Author Ligg
 * @Time 2025/5/30
 **/
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersionEntity> implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

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
}
