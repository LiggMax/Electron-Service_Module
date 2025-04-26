package com.ligg.userweb.controller;

import com.ligg.common.dto.SmsDto;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
public class SmsController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SmsService smsService;

    /**
     * 获取用户短信列表
     */
    @GetMapping
    public Result<List<SmsDto>> getSmsList() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        List<SmsDto> smsList = smsService.getSmsList((Long) map.get("userId"));
        return Result.success(200,smsList);
    }
}
