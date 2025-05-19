package com.ligg.controller;

import com.ligg.common.utils.Result;
import com.ligg.service.adminweb.SmsMassageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 短信提取
 */
@RestController
@RequestMapping("/api/smsMassage")
public class SmsMassageController {

    @Autowired
    private SmsMassageService smsMassageService;

    /**
     * 提取短信号码、验证码
     */
    @PostMapping
    public Result<String> getCode(String sms) {
        smsMassageService.extractCodeAndSms(sms);
        return Result.success();
    }
}
