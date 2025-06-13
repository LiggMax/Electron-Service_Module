package com.ligg.controller;

import com.ligg.common.utils.Result;
import com.ligg.service.common.SmsMassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 短信提取
 */
@Slf4j
@RestController
@RequestMapping("/api/sms")
public class SmsMassageController {


    @Autowired
    private SmsMassageService smsMassageService;


    /**
     * 提取短信号码、验证码
     */
    @PostMapping("/massage")
    public Result<List<Map<String, String>>> getCode(String sms) {
        //提取短信中的验证码和短信号码
        log.info("接收到的初始短信内容：{}", sms);
        List<Map<String, String>> maps = smsMassageService.extractCodeAndSms(sms);

        //保存短信和验证码，更新订单状态
        smsMassageService.saveSmsAndCode(maps);
        return Result.success();
    }
}
