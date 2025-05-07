package com.ligg.controller;

import com.ligg.common.entity.AdminWebUserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理web登录
 */
@RestController
@RequestMapping("/api/adminWeb/account")
public class AdminWebAccountController {

    @Autowired
    private UserService userService;

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
               return Result.success(200,token);
           }
           return Result.error(400,"账号或密码错误");
       }
       return Result.error(400,"账号或密码错误");
    }
}
