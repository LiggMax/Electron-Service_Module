package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ligg.common.entity.AdminWebUserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.JWTUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminUserService;
import com.ligg.service.AdminWebUserService;
import com.ligg.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 后台管理web登录
 */
@RestController
@RequestMapping("/api/adminWeb/account")
public class AdminWebAccountController {

    @Autowired
    private UserService userService;
    @Autowired
    private AdminWebUserService adminWebUserService;
    @Autowired
    private JWTUtil jwtUtil;

    /**
     * 登录
     * return: token
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody AdminWebUserEntity adminWebUserEntity) {

       AdminWebUserEntity adminWebInfo = userService.getAdminWebInfo(adminWebUserEntity.getAccount());
       if (adminWebInfo != null){
           boolean verify = BCryptUtil.verify(adminWebUserEntity.getPassword(), adminWebInfo.getPassword());
           if (verify){
               String token = userService.createToken(adminWebInfo.getAdminId(), adminWebInfo.getAccount());
               UpdateWrapper<AdminWebUserEntity> updateWrapper = new UpdateWrapper<>();
               updateWrapper.eq("admin_id", adminWebInfo.getAdminId())
                       .set("login_time", LocalDateTime.now());
               adminWebUserService.update(updateWrapper);
               return Result.success(200,token);
           }
           return Result.error(400,"账号或密码错误");
       }
       return Result.error(400,"账号或密码错误");
    }

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
}
