package com.ligg.service.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;
import com.ligg.mapper.SmsMapper;
import com.ligg.service.common.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public List<SmsDto> getSmsList(Long userId) {
        return smsMapper.getSmsList(userId);
    }

    /**
     * 获取验证码列表
     */
    @Override
    public List<CodeVo> getCodeList(Long userId) {
        String pattern = "user:orders:" + userId + ":*";
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();

        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

        List<CodeVo> orders = new ArrayList<>();

        while (cursor.hasNext()) {
            byte[] keyBytes = cursor.next();
            String key = new String(keyBytes);

            String json = redisTemplate.opsForValue().get(key);
            if (json != null) {
                try {
                    CodeVo codeVo = objectMapper.readValue(json, CodeVo.class);
                    if (codeVo != null && codeVo.getCode() != null && !codeVo.getCode().isEmpty()) {
                        orders.add(codeVo);
                    }
                } catch (Exception e) {
                    log.error("反序列化订单失败: {}", e.getMessage());
                }
            }
        }

        // 按创建时间降序排序（最新的在前面）
        orders.sort(Comparator.comparing(CodeVo::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        return orders;
    }
}
