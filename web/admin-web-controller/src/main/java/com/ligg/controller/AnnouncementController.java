package com.ligg.controller;

import com.ligg.common.entity.AnnouncementEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * 公告
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    /**
     * 发布公告
     */
    @PostMapping("/publish")
    public Result<String> publish(@RequestBody @Validated AnnouncementEntity announcementEntity) {
        announcementEntity.setCreateTime(LocalDateTime.now());
        announcementService.saveOrUpdate(announcementEntity);
        return Result.success();
    }
}
