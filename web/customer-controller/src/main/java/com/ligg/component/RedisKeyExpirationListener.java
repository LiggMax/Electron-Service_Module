package com.ligg.component;

import com.ligg.service.common.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Redis Key过期监听器
 *
 * @Author Ligg
 * @Time 2025/6/6
 **/
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final Logger log = LoggerFactory.getLogger(RedisKeyExpirationListener.class);
    private final DatabaseService databaseService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer,
                                      DatabaseService databaseService) {
        super(listenerContainer);
        this.databaseService = databaseService;
        log.info("Redis Key过期监听器已初始化");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("检测到Redis key过期: {}", expiredKey);

        String expiredValue = redisTemplate.opsForValue().get(expiredKey);
        log.info("过期key对应的值: {}", expiredValue);
    }
}
