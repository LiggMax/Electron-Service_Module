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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

        orders.sort(Comparator.comparing(CodeVo::getCreatedAt).reversed());
        return orders;
    }
}
