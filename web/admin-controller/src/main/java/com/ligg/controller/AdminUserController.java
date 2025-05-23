package com.ligg.controller;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.OrderVo;
import com.ligg.service.admin.AdminUserService;
import com.ligg.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 退出
     */
    @GetMapping("/logout")
    public Result<String> logout() {
        String token = request.getHeader("Token");
        Map<String, Object> map = jwtUtil.parseToken(token);
        Long userId = (Long) map.get("userId");
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
        Long userId = (Long) map.get("userId");
        AdminUserEntity AdminUserInfo = userService.findByAdminUserInfo(userId);
        return Result.success(200, AdminUserInfo);
    }

    /**
     * 获取订单信息
     */
    @GetMapping("/order")
    public Result<List<OrderVo>> getUserOrder() {
        Map<String, Object> map = jwtUtil.parseToken(request.getHeader("Token"));
        Long AdminId = (Long) map.get("userId");
        List<OrderVo> orderVoList = adminUserService.getOrder(AdminId);
        return Result.success(200,orderVoList);
    }
}
