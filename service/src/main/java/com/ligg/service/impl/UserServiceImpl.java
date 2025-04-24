package com.ligg.service.impl;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.ThreadLocalUtil;
import com.ligg.mapper.UserMapper;
import com.ligg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据账号和密码查询用户信息
     */
    @Override
    public AdminUserEntity findByAdminUser(String account, String password) {
        return userMapper.findByAdminUser(account,password);
    }

    /**
     * 生成token
     */
    @Override
    public String createToken(AdminUserEntity user) {

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId",user.getUserId());
        claims.put("account",user.getAccount());
        String token = jwtUtil.createToken(claims);
        redisTemplate.opsForValue()
                .set("Token:" + user.getUserId(),token,
                        6, TimeUnit.HOURS);
        return token;
    }

    /**
     * 清除token
     */
    @Override
    public void clearToken(String userId) {
        redisTemplate.delete("Token:" + userId);
    }

    /**
     * 根据id获取用户信息
     */
    @Override
    public AdminUserEntity findByAdminUserInfo(String userId) {

        return userMapper.findByAdminUserInfo(userId);
    }
}
