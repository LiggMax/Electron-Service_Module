package com.ligg.controller;

import com.ligg.common.dto.OrdersDto;
import com.ligg.common.dto.SmsDto;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.CodeVo;
import com.ligg.service.common.SmsMassageService;
import com.ligg.service.common.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 用户号码管理
 */
@Slf4j
@RestController
@RequestMapping("/api/user/sms")
public class SmsController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SmsService smsService;

    /**
     * 获取用户号码列表
     */
    @GetMapping
    public Result<List<SmsDto>> getSmsList() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        List<SmsDto> smsList = smsService.getSmsList((Long) map.get("userId"));
        return Result.success(200, smsList);
    }

    /**
     * 获取用户定订单验证码
     */
    @GetMapping("/code")
    public Result<List<CodeVo>> getCodeList() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        //获取用户订单列表
        List<CodeVo> codeList = smsService.getCodeList((Long) map.get("userId"));

        return Result.success(200, codeList);
    }
}
