package com.ligg.component;

import com.ligg.service.common.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Redis Key过期监听器
 *
 * @Author Ligg
 * @Time 2025/6/6
 **/
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

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

        try {
            // 根据业务规则处理不同的key
            if (expiredKey.startsWith("your_prefix:")) {
                String businessId = expiredKey.replace("your_prefix:", "");
                log.info("处理业务ID: {}", businessId);
                databaseService.updateOrDeleteData(businessId);
                log.info("成功处理过期key对应的业务数据: {}", businessId);
            } else {
                log.debug("忽略不匹配前缀的key: {}", expiredKey);
            }
        } catch (Exception e) {
            log.error("处理过期key时发生错误: {}, 错误信息: {}", expiredKey, e.getMessage(), e);
        }
    }
}
