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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
     * 获取用户定订单验证码
     */
    @GetMapping("/code")
    public Result<List<CodeVo>> getCodeList() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        //获取用户订单列表
        List<CodeVo> codeList = smsService.getCodeList((Long) map.get("userId"));
        return Result.success(BusinessStates.SUCCESS, codeList);
    }

    /**
     * SSE连接端点 - 实时推送短信验证码
     */
    @GetMapping("/sse")
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
            log.info("用户 {} SSE连接已关闭", userId);
        });

        // 设置连接超时回调
        emitter.onTimeout(() -> {
            sseEmitters.remove(userId);
            log.info("用户 {} SSE连接超时", userId);
        });

        // 设置连接错误回调
        emitter.onError((throwable) -> {
            sseEmitters.remove(userId);
            log.error("用户 {} SSE连接发生错误", userId, throwable);
        });

        // 启动定时任务，模拟实时推送验证码
        startSmsCodePushTask(userId);

        return emitter;
    }

    /**
     * 启动短信验证码推送任务
     */
    private void startSmsCodePushTask(Long userId) {
        scheduler.scheduleAtFixedRate(() -> {
            SseEmitter emitter = sseEmitters.get(userId);
            if (emitter != null) {
                try {
                    // 获取最新的验证码列表
                    List<CodeVo> codeList = smsService.getCodeList(userId);

                    // 模拟新验证码到达
                    if (!codeList.isEmpty()) {
                        CodeVo latestCode = codeList.get(0);

                        // 推送验证码消息
                        emitter.send(SseEmitter.event()
                                .name("sms-code")
                                .data(Map.of(
                                        "codeInfo", latestCode,
                                        "timestamp", System.currentTimeMillis(),
                                        "message", "收到新的短信验证码"
                                )));

                        log.info("向用户 {} 推送验证码", userId);
                    }

                    // 发送心跳消息
                    emitter.send(SseEmitter.event()
                            .name("heartbeat")
                            .data("heartbeat"));

                } catch (IOException e) {
                    log.error("向用户 {} 推送SSE消息失败", userId, e);
                    sseEmitters.remove(userId);
                } catch (Exception e) {
                    log.error("处理用户 {} 的SSE推送任务时发生错误", userId, e);
                }
            }
        }, 5, 10, TimeUnit.SECONDS); // 5秒后开始，每10秒执行一次
    }

    /**
     * 手动推送消息给指定用户
     */
    public void pushMessageToUser(Long userId, String eventName, Object data) {
        SseEmitter emitter = sseEmitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
                log.info("向用户 {} 推送消息: {}", userId, data);
            } catch (IOException e) {
                log.error("向用户 {} 推送消息失败", userId, e);
                sseEmitters.remove(userId);
            }
        }
    }

    /**
     * 广播消息给所有连接的用户
     */
    public void broadcastMessage(String eventName, Object data) {
        sseEmitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name(eventName)
                        .data(data));
            } catch (IOException e) {
                log.error("向用户 {} 广播消息失败", userId, e);
                sseEmitters.remove(userId);
            }
        });
        log.info("广播消息给 {} 个用户: {}", sseEmitters.size(), data);
    }
}
