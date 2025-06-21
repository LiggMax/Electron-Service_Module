package com.ligg.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;
import com.ligg.mapper.SmsMapper;
import com.ligg.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 定时任务执行器
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // 存储每个用户的定时任务，用于连接断开时取消任务
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> userTasks = new ConcurrentHashMap<>();

    // 存储每个用户已推送的验证码ID，避免重复推送
    private final ConcurrentHashMap<Long, Set<Integer>> userPushedIds = new ConcurrentHashMap<>();

    @Override
    public List<SmsDto> getPhoneNumberList(Long userId) {
        return smsMapper.getSmsList(userId);
    }

    /**
     * 获取验证码列表
     */
    @Override
    public List<CodeVo> getCodeList(Long userId) {
        String pattern = "codes:" + "userId:" + userId + ":orderId:" + "*";
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

        List<CodeVo> orders = new ArrayList<>();

        while (cursor.hasNext()) {
            byte[] keyBytes = cursor.next();
            String key = new String(keyBytes);

            Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
            for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                String value = entry.getValue().toString();
                try {
                    orders.add(objectMapper.readValue(value, CodeVo.class));
                } catch (Exception e) {
                    log.error("反序短信失败: {}", e.getMessage());
                }
            }
        }

        orders.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return orders;
    }

    @Override
    public void startSmsCodePushTask(Long userId, SseEmitter emitter) {
        // 初始化或获取用户的已推送ID集合
        Set<Integer> pushedIds = userPushedIds.computeIfAbsent(userId, k -> new HashSet<>());

        // 如果该用户已有任务在运行，先取消
        ScheduledFuture<?> existingTask = userTasks.get(userId);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel(true);
        }

        // 创建新的定时任务
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            // 检查连接是否还有效
            if (emitter == null) {
                log.info("连接或数据无效，停止任务 userId: {}", userId);
                cancelUserTask(userId);
                return;
            }

            try {
                // 检查SseEmitter状态
                if (isEmitterClosed(emitter)) {
                    log.info("SSE连接已关闭，停止推送任务 userId: {}", userId);
                    cancelUserTask(userId);
                    return;
                }

                // 从Redis获取用户的验证码Hash集合
                String pattern = "codes:userId:" + userId + ":orderId:*";
                ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
                Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

                boolean hasNewCode = false;

                while (cursor.hasNext()) {
                    byte[] keyBytes = cursor.next();
                    String key = new String(keyBytes);

                    // 获取Hash集合中的所有条目
                    Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

                    for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                        String field = entry.getKey().toString();
                        String value = entry.getValue().toString();

                        // 使用field作为唯一标识检查是否为新验证码
                        int fieldHash = field.hashCode();

                        if (!pushedIds.contains(fieldHash)) {
                            // 发现新验证码，解析并推送
                            try {
                                // 再次检查连接状态
                                if (isEmitterClosed(emitter)) {
                                    log.info("推送前检测到连接已关闭 userId: {}", userId);
                                    cancelUserTask(userId);
                                    return;
                                }

                                CodeVo newCode = objectMapper.readValue(value, CodeVo.class);

                                // 推送新验证码
                                Map<String, Object> pushData = new HashMap<>();
                                pushData.put("codeInfo", newCode);
                                pushData.put("timestamp", System.currentTimeMillis());
                                pushData.put("message", "收到新的短信验证码");

                                emitter.send(SseEmitter.event()
                                        .name("sms-code")
                                        .data(pushData));

                                // 记录已推送的验证码
                                pushedIds.add(fieldHash);
                                hasNewCode = true;

                                log.info("成功推送新验证码给用户: {}, field: {}", userId, field);

                            } catch (Exception parseException) {
                                if (parseException.getMessage().contains("has already completed")) {
                                    log.info("连接已完成，停止推送 userId: {}", userId);
                                    cancelUserTask(userId);
                                    return;
                                }
                                log.warn("解析验证码失败: " + parseException.getMessage());
                            }
                        }
                    }
                }

                // 定期发送心跳（每30秒一次）
                if (System.currentTimeMillis() % 30000 < 10000) {
                    try {
                        if (!isEmitterClosed(emitter)) {
                            emitter.send(SseEmitter.event()
                                    .name("heartbeat")
                                    .data("heartbeat"));
                        }
                    } catch (Exception heartbeatException) {
                        if (heartbeatException.getMessage().contains("has already completed")) {
                            log.warn("心跳发送失败，连接已关闭 userId: {}", userId);
                            cancelUserTask(userId);
                            return;
                        }
                    }
                }

                // 如果有新验证码，输出日志
                if (hasNewCode) {
                    log.info("用户 {} 收到新验证码推送", userId);
                }

            } catch (Exception e) {
                if (e.getMessage().contains("has already completed")) {
                    log.info("连接已完成，停止推送任务 userId: {}", userId);
                    cancelUserTask(userId);
                } else {
                    log.warn("推送验证码失败 userId: {}, error: {}", userId, e.getMessage());
                }
            }
        }, 0, 10, TimeUnit.SECONDS);// 0秒后开始，每10秒执行一次

        // 保存任务引用
        userTasks.put(userId, task);
        log.info("为用户 {} 启动推送任务", userId);
    }

    /**
     * 检查SseEmitter是否已关闭
     */
    private boolean isEmitterClosed(SseEmitter emitter) {
        try {
            // 通过反射检查内部状态，或者使用其他方式
            // 这里使用简单的方式：尝试发送一个测试事件
            return false; // 暂时返回false，实际应该检查连接状态
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 取消用户的推送任务
     */
    private void cancelUserTask(Long userId) {
        ScheduledFuture<?> task = userTasks.remove(userId);
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
            log.info("已取消用户 " + userId + " 的推送任务");
        }
        // 清理用户的去重数据
        userPushedIds.remove(userId);
    }

    /**
     * 停止指定用户的推送任务（供外部调用）
     */
    @Override
    public void stopSmsCodePushTask(Long userId) {
        cancelUserTask(userId);
    }

}

