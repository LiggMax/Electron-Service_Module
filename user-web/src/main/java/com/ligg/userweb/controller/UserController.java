package com.ligg.userweb.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public Result<UserEntity> getUserInfo() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) map.get("userId");
        UserEntity userInfo = userService.findByUserInfo(userId);
        return Result.success(200,userInfo);
    }
}
