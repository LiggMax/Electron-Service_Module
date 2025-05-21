package com.ligg.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.AnnouncementEntity;

import java.util.List;

public interface AnnouncementService extends IService<AnnouncementEntity> {
    // 用户获取公告
    List<AnnouncementEntity> UserGetAnnouncement();
}
