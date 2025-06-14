package com.ligg.entrance.interceptors;

import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

/**
 * 登录拦截器
 */
@Slf4j
@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求头中的token
        String Token = request.getHeader("Token");

        try {
            //解析请求头token获取用户信息
            Map<String, Object> stringObjectMap = jwtUtil.parseToken(Token);
            System.out.println(stringObjectMap.get("userId"));
            Long userId = (Long) stringObjectMap.get("userId");
            //从Redis中获取用户token
            String redisUserToken = redisTemplate.opsForValue().get("Token:" + userId);
            if (redisUserToken == null){
                throw new RuntimeException();
            }
//            Map<String, Object> claims = jwtUtil.parseToken(Token);
//            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            log.error("Token validation failed: ",e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  {
        //清除ThreadLocal数据
        ThreadLocalUtil.remove();
    }
}
