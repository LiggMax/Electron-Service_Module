package com.ligg.component;

import com.ligg.common.entity.OrderEntity;
import com.ligg.service.CustomerOrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private CustomerOrdersService customerOrdersService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
        log.info("Redis Key过期监听器已初始化");
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        //只处理以 "user:orders:" 开头的key 订单过期
        if (!expiredKey.startsWith("user:orders:")) {
            return;
        }
        String[] parts = expiredKey.split(":");
        if (parts.length == 4) {
            // 订单ID
            String orderId = parts[3];
            OrderEntity orderInfo = customerOrdersService.getOrderById(orderId);

            if (orderInfo == null) {
                log.warn("订单{}不存在", orderId);
                return;
            }
            /*
             * 如果订单过期还未使用，则删除订单信息 , 并返回订单金额给用户
             */
            if (orderInfo.getState() == 0) {
                log.info("检测到用户:{}订单过期未使用，订单id{},退款金额{}",
                        orderInfo.getUserId(), orderId, orderInfo.getProjectMoney());

                //删除订单信息
                customerOrdersService.deleteOrderById(orderId);
                //退款并回滚号码项目关联状态为可用
                customerOrdersService.refundOrderAndUpdateRelation(orderInfo);
            } else if (orderInfo.getState() == 1) {

                log.info("订单{} 已使用，更新状态为待结算", orderId);
                customerOrdersService.updateOrderStateById(orderId, 2);
            }
        }
    }
}
