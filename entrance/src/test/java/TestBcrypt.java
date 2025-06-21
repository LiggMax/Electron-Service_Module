import com.ligg.common.utils.BCryptUtil;
import com.ligg.entrance.EntranceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = EntranceApplication.class)
public class TestBcrypt {

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 测试加密
    @Test
    public void testEncrypt() {
        String password = "admin";
        String encrypt = BCryptUtil.encrypt(password);
        log.info("加密结果: {}", encrypt);
    }

    // 测试redis过期监听
    @Test
    public void testRedis() {
        redisTemplate.opsForValue().set("过期的key:", "测试过期监听", 10, TimeUnit.SECONDS);
    }

    // 测试Redis key过期监听功能
    @Test
    public void testRedisKeyExpiration() throws InterruptedException {
        String testKey = "your_prefix:test_business_id_123";
        String testValue = "测试数据";

        log.info("设置Redis key: {} 过期时间: 3秒", testKey);
        // 设置一个较短的过期时间用于测试
        redisTemplate.opsForValue().set(testKey, testValue, 3, TimeUnit.SECONDS);

        // 验证key确实被设置了
        String value = redisTemplate.opsForValue().get(testKey);
        log.info("设置后立即查询key值: {}", value);

        // 等待key过期
        log.info("等待key过期...");
        Thread.sleep(4000); // 等待4秒确保key已过期

        // 验证key已过期
        String expiredValue = redisTemplate.opsForValue().get(testKey);
        log.info("过期后查询key值: {}", expiredValue);
        
        if (expiredValue == null) {
            log.info("✅ 测试成功: key已过期，过期监听器应该已被触发");
        } else {
            log.warn("⚠️ 测试警告: key未按预期过期");
        }
    }

    // 测试多个key的过期监听
    @Test
    public void testMultipleKeysExpiration() throws InterruptedException {
        log.info("开始测试多个key的过期监听...");

        // 设置多个测试key
        for (int i = 1; i <= 3; i++) {
            String key = "your_prefix:batch_test_" + i;
            String value = "批量测试数据_" + i;
            redisTemplate.opsForValue().set(key, value, 2, TimeUnit.SECONDS);
            log.info("设置key: {}, 值: {}", key, value);
        }

        // 设置一个不匹配前缀的key
        redisTemplate.opsForValue().set("other_prefix:test", "其他数据", 2, TimeUnit.SECONDS);
        log.info("设置不匹配前缀的key: other_prefix:test");

        // 等待所有key过期
        log.info("等待所有key过期...");
        Thread.sleep(3000);

        log.info("✅ 批量过期测试完成，请检查控制台输出确认监听器是否正确处理了相关key");
    }

    // 综合测试：验证完整的过期监听和处理流程
    @Test
    public void testCompleteExpirationFlow() throws InterruptedException {
        log.info("🚀 开始综合测试Redis key过期监听完整流程...");

        // 准备测试数据
        String businessId = "test_order_12345";
        String mainKey = "your_prefix:" + businessId;
        String cacheKey = "cache:" + businessId;
        String historyKey = "history:" + businessId;

        // 1. 设置主要业务key和相关缓存
        redisTemplate.opsForValue().set(mainKey, "订单数据", 5, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(cacheKey, "订单缓存数据", 10, TimeUnit.MINUTES);
        log.info("📝 已设置业务key: {} 和缓存key: {}", mainKey, cacheKey);

        // 2. 验证初始状态
        log.info("📊 初始状态检查:");
        log.info("   主key存在: {}", redisTemplate.hasKey(mainKey));
        log.info("   缓存key存在: {}", redisTemplate.hasKey(cacheKey));
        log.info("   历史key存在: {}", redisTemplate.hasKey(historyKey));

        // 3. 等待主key过期（这会触发监听器）
        log.info("⏰ 等待主key过期并触发监听器...");
        Thread.sleep(6000); // 等待6秒，确保主key过期

        // 4. 验证过期后的状态
        log.info("📊 过期后状态检查:");
        log.info("   主key存在: {}", redisTemplate.hasKey(mainKey));
        log.info("   缓存key存在: {}", redisTemplate.hasKey(cacheKey));
        log.info("   历史key存在: {}", redisTemplate.hasKey(historyKey));

        // 5. 检查历史记录（由监听器创建）
        String historyValue = redisTemplate.opsForValue().get(historyKey);
        if (historyValue != null) {
            log.info("✅ 发现历史记录: {}", historyValue);
            log.info("🎉 过期监听器成功处理了业务数据!");
        } else {
            log.warn("⚠️ 未发现历史记录，可能监听器未正确工作");
        }

        log.info("�� 综合测试完成");
    }
}
