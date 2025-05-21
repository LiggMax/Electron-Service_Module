package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.AnnouncementEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.common.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 公告
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;
    /**
     * 发布、编辑公告
     */
    @PostMapping("/publishOrUpdate")
    public Result<String> publish(@RequestBody @Validated AnnouncementEntity announcementEntity) {
        announcementEntity.setCreateTime(LocalDateTime.now());
        announcementService.saveOrUpdate(announcementEntity);
        return Result.success();
    }

    /**
     * 获取公告
     */
    @GetMapping
    public Result<List<AnnouncementEntity>> getAnnouncement() {
        return Result.success(200,announcementService.getBaseMapper()
                .selectList(new LambdaQueryWrapper<AnnouncementEntity>()
                        .orderByDesc(AnnouncementEntity::getCreateTime)));
    }

    /**
     *删除公告
     */
    @DeleteMapping
    public Result<String> deleteAnnouncement(@RequestParam Long id) {
        announcementService.removeById(id);
        return Result.success();
    }
}
