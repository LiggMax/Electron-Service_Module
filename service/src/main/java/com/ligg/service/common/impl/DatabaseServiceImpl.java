package com.ligg.service.common.impl;

import com.ligg.service.common.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 数据库服务实现类
 *
 * @Author Ligg
 * @Time 2025/6/6
 **/
@Service
public class DatabaseServiceImpl implements DatabaseService {

    private static final Logger log = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void updateOrDeleteData(String businessId) {
        log.info("开始处理业务数据，业务ID: {}", businessId);

        try {
            // 模拟数据库操作
            // 这里可以根据实际需求进行数据库更新或删除操作

            // 示例：清理相关的缓存数据
            String cacheKey = "cache:" + businessId;
            Boolean deleted = redisTemplate.delete(cacheKey);
            if (deleted) {
                log.info("成功清理相关缓存: {}", cacheKey);
            }

            // 示例：记录处理历史
            String historyKey = "history:" + businessId;
            String timestamp = String.valueOf(System.currentTimeMillis());
            redisTemplate.opsForValue().set(historyKey, "过期处理时间:" + timestamp);

            log.info("✅ 业务数据处理完成，业务ID: {}", businessId);

        } catch (Exception e) {
            log.error("❌ 处理业务数据时发生错误，业务ID: {}, 错误信息: {}", businessId, e.getMessage(), e);
            throw e;
        }
    }
}
