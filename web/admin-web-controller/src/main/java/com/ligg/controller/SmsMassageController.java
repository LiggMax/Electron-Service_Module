package com.ligg.controller;

import com.ligg.common.utils.Result;
import com.ligg.service.adminweb.SmsMassageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 短信提取
 */
@RestController
@RequestMapping("/api/sms")
public class SmsMassageController {

    @Autowired
    private SmsMassageService smsMassageService;

    /**
     * 提取短信号码、验证码
     */
    @PostMapping("/massage")
    public Result<String> getCode(String sms) {
        //提取短信中的验证码和短信号码
        List<Map<String, String>> maps = smsMassageService.extractCodeAndSms(sms);
        //保存短信和验证码
        smsMassageService.saveSmsAndCode(maps);
        return Result.success();
    }
}
