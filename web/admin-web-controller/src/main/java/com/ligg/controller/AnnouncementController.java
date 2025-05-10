package com.ligg.controller;

import com.ligg.common.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公告
 */
@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    /**
     * 发布公告
     */
    @PostMapping("/publish")
    public Result<String> publish() {

        return new Result<>();
    }
}
