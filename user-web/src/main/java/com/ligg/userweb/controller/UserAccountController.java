package com.ligg.userweb.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user/account")
public class UserAccountController {

    @Autowired
    private UserService userService;
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String,Object> user){

        if (! (user.get("account") instanceof String account && user.get("password") instanceof String password)){
            return Result.error(400,"参数错误");
        }

        UserEntity byAccount = userService.findByUser(account, password);
        if (byAccount != null){
            String userToken = userService.createToken(byAccount.getUserId(), byAccount.getAccount());
            return Result.success(200,userToken);
        }
        return Result.error(400,"账号或密码错误");
    }
}
