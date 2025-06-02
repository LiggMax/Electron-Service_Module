package com.ligg.service.common.impl;

import com.ligg.common.utils.JWTUtil;
import com.ligg.service.common.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author Ligg
 * @Time 2025/6/2
 **/
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public String createToken(Long userId, String account) {

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("account", account);
        String token = jwtUtil.createToken(claims);
        redisTemplate.opsForValue()
                .set("Token:" + userId, token,
                        6, TimeUnit.HOURS);
        return token;
    }

    /**
     * 清除token
     */
    @Override
    public void clearToken(Long userId) {
        redisTemplate.delete("Token:" + userId);
    }
}
