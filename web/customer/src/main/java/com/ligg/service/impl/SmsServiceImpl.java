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
    public void startSmsCodePushTask(Long userId, SseEmitter emitter, Set<Integer> pushedIds) {
        scheduler.scheduleAtFixedRate(() -> {
            if (emitter != null && pushedIds != null) {
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

                                    System.out.println("推送新验证码给用户: " + userId + ", field: " + field);

                                } catch (Exception parseException) {
                                    System.err.println("解析验证码失败: " + parseException.getMessage());
                                }
                            }
                        }
                    }

                    // 定期发送心跳（每30秒一次）
                    if (System.currentTimeMillis() % 30000 < 10000) {
                        emitter.send(SseEmitter.event()
                                .name("heartbeat")
                                .data("heartbeat"));
                    }

                    // 如果有新验证码，输出日志
                    if (hasNewCode) {
                        System.out.println("用户 " + userId + " 收到新验证码推送");
                    }

                } catch (Exception e) {
                    System.err.println("推送验证码失败 userId: " + userId + ", error: " + e.getMessage());
                }
            }
        }, 5, 10, TimeUnit.SECONDS); // 5秒后开始，每10秒检查一次
    }


}

