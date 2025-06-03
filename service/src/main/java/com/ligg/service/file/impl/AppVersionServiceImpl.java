package com.ligg.service.file.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AppVersionEntity;
import com.ligg.common.entity.version.AppVersion;
import com.ligg.mapper.AppVersionMapper;
import com.ligg.service.file.AppVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 版本管理服务实现类
 */
@Slf4j
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersionEntity> implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    /**
     * 检查版本更新
     */
    @Override
    public List<AppVersion> getAppVersionList(String currentVersion) {
        try {
            // 查询所有版本，按更新时间倒序
            LambdaQueryWrapper<AppVersionEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(AppVersionEntity::getUploadTime);

            List<AppVersionEntity> allVersions = appVersionMapper.selectList(queryWrapper);

            // 如果当前版本为空，返回最新版本
            if (currentVersion == null || currentVersion.trim().isEmpty()) {
                if (!allVersions.isEmpty()) {
                    AppVersion latestVersion = convertToAppVersion(allVersions.get(0));
                    return Collections.singletonList(latestVersion);
                }
                return Collections.emptyList();
            }

            // 过滤出比当前版本更新的版本
            List<AppVersionEntity> newerVersions = allVersions.stream()
                    .filter(version -> compareVersion(version.getVersion(), currentVersion) > 0)
                    .toList();

            // 转换为AppVersion对象
            return newerVersions.stream()
                    .map(this::convertToAppVersion)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * 版本号比较
     * 返回值：1表示version1 > version2，0表示相等，-1表示version1 < version2
     */
    private int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }

        try {
            // 去除可能的'v'前缀
            version1 = version1.toLowerCase().replaceFirst("^v", "");
            version2 = version2.toLowerCase().replaceFirst("^v", "");

            String[] v1Parts = version1.split("\\.");
            String[] v2Parts = version2.split("\\.");

            int maxLength = Math.max(v1Parts.length, v2Parts.length);

            for (int i = 0; i < maxLength; i++) {
                int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
                int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

                if (v1Part > v2Part) {
                    return 1;
                } else if (v1Part < v2Part) {
                    return -1;
                }
            }
            return 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 实体类转换为VO
     */
    private AppVersion convertToAppVersion(AppVersionEntity entity) {
        AppVersion appVersion = new AppVersion();
        BeanUtils.copyProperties(entity, appVersion);
        appVersion.setId(String.valueOf(entity.getId()));
        return appVersion;
    }

    /**
     * 获取版本列表
     */
    @Override
    public Map<String, Object> getVersionList(Integer page, Integer size) {
        Page<AppVersionEntity> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<AppVersionEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(AppVersionEntity::getUploadTime);

        Page<AppVersionEntity> result = appVersionMapper.selectPage(pageInfo, queryWrapper);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());
        data.put("page", page);
        data.put("size", size);
        data.put("pages", result.getPages());

        return data;
    }

    /**
     * 保存版本信息
     */
    @Override
    public void saveVersion(String version, String releaseNotes, String downloadUrl, Long fileSize, Integer app, LocalDateTime updateTime) {
        AppVersionEntity appVersion = new AppVersionEntity();
        appVersion.setVersion(version);
        appVersion.setDownloadUrl(downloadUrl);
        appVersion.setReleaseNotes(releaseNotes);
        appVersion.setFileSize(fileSize);
        appVersion.setApp(app);
        appVersion.setUploadTime(updateTime);
        appVersionMapper.insert(appVersion);
    }
}
