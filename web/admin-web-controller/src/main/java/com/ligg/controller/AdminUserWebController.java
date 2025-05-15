package com.ligg.controller;

import com.ligg.common.entity.AdminWebUserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.adminweb.AdminWebUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/adminUserWeb")
public class AdminUserWebController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AdminWebUserService adminWebUserService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 获取用户想信息
     */
    @GetMapping("/info")
    public Result<AdminWebUserEntity> getUserInfo(HttpServletRequest request) {
        Map<String, Object> userMap = jwtUtil.parseToken(request.getHeader("Token"));
        Long userId = (Long) userMap.get("userId");
        AdminWebUserEntity userInfo = adminWebUserService.getById(userId);
        userInfo.setPassword(null);
        return Result.success(200,userInfo);
    }

    /**
     * 资料编辑
     */
    @PostMapping("/edit")
    public Result<String> editUserInfo(@RequestBody @Validated AdminWebUserEntity adminWebUserEntity) {
        Map<String, Object> userMap = jwtUtil.parseToken(request.getHeader("Token"));
        if(userMap.get("userId") == null) {
            return Result.error(400,"用户数据错误");
        }
        adminWebUserEntity.setAdminId((Long) userMap.get("userId"));
        adminWebUserService.updateById(adminWebUserEntity);
        return Result.success();
    }
}
