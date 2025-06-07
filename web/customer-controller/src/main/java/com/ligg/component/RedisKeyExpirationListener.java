package com.ligg.component;

import com.ligg.common.entity.OrderEntity;
import com.ligg.service.CustomerOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Redis Key过期监听器
 *
 * @Author Ligg
 * @Time 2025/6/6
 **/
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CustomerOrdersService customerOrdersService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
        log.info("Redis Key过期监听器已初始化");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.info("检测到Redis key过期: {}", expiredKey);

        String[] parts = expiredKey.split(":");
        if (parts.length == 4) {
            // 订单ID
            String orderId = parts[3];
            OrderEntity orderInfo = customerOrdersService.getOrderById(orderId);

            /*
             * 如果订单过期还未使用，则删除订单信息 , 并返回订单金额给用户
             */
            if (orderInfo.getCode() == null || orderInfo.getCode().isEmpty()) {
                //删除订单信息
                customerOrdersService.deleteOrderById(orderId);

                //退款
                customerOrdersService.refundOrder(orderInfo);
            }
        }
    }
}
