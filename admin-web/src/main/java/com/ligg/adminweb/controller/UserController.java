package com.ligg.adminweb.controller;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.utils.ThreadLocalUtil;
import com.ligg.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;

    /**
     * 退出
     */
    @GetMapping("/logout")
    public Result<String> logout() {
        String token = request.getHeader("Token");
        Map<String, Object> map = jwtUtil.parseToken(token);
        String userId = (String) map.get("userId");
        userService.clearToken(userId);
        return Result.success(200, "退出成功");
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<AdminUserEntity> getUserInfo() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        if (map == null){
            return Result.error(401, "请重新登录");
        }
        String userId = (String) map.get("userId");
        AdminUserEntity AdminUserInfo = userService.findByAdminUserInfo(userId);
        return Result.success(200, AdminUserInfo);
    }

}
