package com.ligg.controller;

import com.ligg.common.dto.SmsDto;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.CodeVo;
import com.ligg.service.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 用户号码管理
 */
@RestController
@RequestMapping("/api/user/sms")
public class SmsController {

    private static final Logger log = LoggerFactory.getLogger(SmsController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SmsService smsService;

    // 存储用户的SSE连接
    private final ConcurrentHashMap<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    /**
     * 获取用户号码列表
     */
    @GetMapping
    public Result<List<SmsDto>> getSmsList() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        List<SmsDto> smsList = smsService.getPhoneNumberList((Long) map.get("userId"));
        return Result.success(BusinessStates.SUCCESS, smsList);
    }

    /**
     * SSE连接端点 - 实时推送短信验证码
     */
    @GetMapping("/sse/code")
    public SseEmitter smsSSE(@RequestParam String token) {
        Map<String, Object> map = jwtUtil.parseToken(token);
        Long userId = (Long) map.get("userId");

        // 创建SSE连接，设置超时时间为30分钟
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        // 存储用户连接
        sseEmitters.put(userId, emitter);

        log.info("用户 {} 建立SSE连接", userId);

        // 连接建立时发送欢迎消息
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE连接建立成功，开始监听短信验证码"));
        } catch (IOException e) {
            log.error("发送SSE连接确认消息失败", e);
        }

        // 设置连接完成回调
        emitter.onCompletion(() -> {
            sseEmitters.remove(userId);
            smsService.stopSmsCodePushTask(userId);
            log.info("用户 {} SSE连接已关闭", userId);
        });

        // 设置连接超时回调
        emitter.onTimeout(() -> {
            sseEmitters.remove(userId);
            smsService.stopSmsCodePushTask(userId);
            log.info("用户 {} SSE连接超时", userId);
        });

        // 设置连接错误回调
        emitter.onError((throwable) -> {
            sseEmitters.remove(userId);
            smsService.stopSmsCodePushTask(userId);
            log.error("用户 {} SSE连接发生错误", userId, throwable);
        });

        // 启动定时任务，实时推送验证码
        smsService.startSmsCodePushTask(userId, emitter);
        return emitter;
    }
}
