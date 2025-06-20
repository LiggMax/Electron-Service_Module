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

    @Override
    public void startSmsCodePushTask(Long userId, SseEmitter emitter) {
        // 初始化或获取用户的已推送ID集合
        Set<Integer> pushedIds = userPushedIds.computeIfAbsent(userId, k -> new HashSet<>());

        // 如果该用户已有任务在运行，先取消
        ScheduledFuture<?> existingTask = userTasks.get(userId);
        if (existingTask != null && !existingTask.isCancelled()) {
            existingTask.cancel(true);
            // 清理之前的推送记录，重新开始
            pushedIds.clear();
        }

        // 立即推送所有历史验证码
        pushHistoricalCodes(userId, emitter, pushedIds);

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

                // 推送新的验证码
                pushNewCodes(userId, emitter, pushedIds);

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
     * 推送历史验证码
     */
    private void pushHistoricalCodes(Long userId, SseEmitter emitter, Set<Integer> pushedIds) {
        try {
            // 从Redis获取用户的验证码Hash集合
            String pattern = "codes:userId:" + userId + ":orderId:*";
            ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
            Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

            List<CodeVo> historicalCodes = new ArrayList<>();

            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                String key = new String(keyBytes);

                // 获取Hash集合中的所有条目
                Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);

                for (Map.Entry<Object, Object> entry : entries.entrySet()) {
                    String field = entry.getKey().toString();
                    String value = entry.getValue().toString();

                    try {
                        CodeVo code = objectMapper.readValue(value, CodeVo.class);
                        historicalCodes.add(code);

                        // 使用field的hashCode作为唯一标识
                        int fieldHash = field.hashCode();
                        pushedIds.add(fieldHash);

                    } catch (Exception e) {
                        // 解析失败跳过
                    }
                }
            }

            // 按时间倒序排列
            historicalCodes.sort((a, b) -> {
                // 简单的时间比较，如果没有时间字段就保持原顺序
                return 0;
            });

            if (!historicalCodes.isEmpty()) {
                log.info("为用户 {} 推送 {} 条历史验证码", userId, historicalCodes.size());

                for (CodeVo code : historicalCodes) {
                    try {
                        // 推送历史验证码
                        Map<String, Object> pushData = new HashMap<>();
                        pushData.put("codeInfo", code);
                        pushData.put("timestamp", System.currentTimeMillis());
                        pushData.put("message", "历史短信验证码");
                        pushData.put("isHistorical", true); // 标记为历史数据

                        emitter.send(SseEmitter.event()
                                .name("sms-code")
                                .data(pushData));

                    } catch (Exception e) {
                        if (e.getMessage().contains("has already completed")) {
                            log.warn("推送历史数据时连接已关闭 userId: {}", userId);
                            return;
                        }
                    }
                }

                log.info("用户 {} 历史验证码推送完成", userId);
            } else {
                log.info("用户 {} 暂无历史验证码", userId);
            }

        } catch (Exception e) {
            log.warn("获取历史验证码失败 userId: {}, error: {}", userId, e.getMessage());
        }
    }

    /**
     * 推送新验证码
     */
    private void pushNewCodes(Long userId, SseEmitter emitter, Set<Integer> pushedIds) {
        try {
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
                                System.out.println("推送前检测到连接已关闭 userId: " + userId);
                                cancelUserTask(userId);
                                return;
                            }

                            CodeVo newCode = objectMapper.readValue(value, CodeVo.class);

                            // 推送新验证码
                            Map<String, Object> pushData = new HashMap<>();
                            pushData.put("codeInfo", newCode);
                            pushData.put("timestamp", System.currentTimeMillis());
                            pushData.put("message", "收到新的短信验证码");
                            pushData.put("isHistorical", false); // 标记为新数据

                            emitter.send(SseEmitter.event()
                                    .name("sms-code")
                                    .data(pushData));

                            // 记录已推送的验证码
                            pushedIds.add(fieldHash);
                            hasNewCode = true;

                            System.out.println("成功推送新验证码给用户: " + userId + ", field: " + field);

                        } catch (Exception parseException) {
                            if (parseException.getMessage().contains("has already completed")) {
                                System.out.println("连接已完成，停止推送 userId: " + userId);
                                cancelUserTask(userId);
                                return;
                            }
                        }
                    }
                }
            }

            // 如果有新验证码，输出日志
            if (hasNewCode) {
                System.out.println("用户 " + userId + " 收到新验证码推送");
            }

        } catch (Exception e) {
            System.err.println("推送新验证码失败 userId: " + userId + ", error: " + e.getMessage());
        }
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

