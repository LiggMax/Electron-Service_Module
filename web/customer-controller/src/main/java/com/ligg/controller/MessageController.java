package com.ligg.controller;

import com.ligg.common.entity.adminweb.AnnouncementEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.common.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 客户消息消息
 */
@RestController
@RequestMapping("/api/user/message")
public class MessageController {

    @Autowired
    private AnnouncementService announcementService;

    /**
     * 获取公告
     */
    @GetMapping("/announcement")
    public Result<List<AnnouncementEntity>> getAnnouncement() {
       List<AnnouncementEntity>  announcementEntity = announcementService.UserGetAnnouncement();
       return Result.success(200,announcementEntity);
    }
}
