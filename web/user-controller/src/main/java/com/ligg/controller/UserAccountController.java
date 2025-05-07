package com.ligg.controller;

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
    public Result<String> login(@RequestBody Map<String, Object> user) {

        if (!(user.get("account") instanceof String account && user.get("password") instanceof String password)) {
            return Result.error(400, "参数错误");
        }
        UserEntity byAccount = userService.findAccountAndPasswordByUser(account,password);
        if (byAccount != null){
            if (byAccount.getUserStatus() == 0) {
                return Result.error(400, "该账号已被注销");
            }
            return Result.success(200, userService.createToken(byAccount.getUserId(), byAccount.getAccount()));
        }
        return Result.error(400, "账号或密码错误");
    }

    /**
     * 账户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody Map<String, String> user) {
        //校验account和password长度不小于6位
        if (user.get("account").length() < 6 || user.get("password").length() < 6) {
            return Result.error(400, "账号或密码长度不小于6位");
        }
        UserEntity byUser = userService.findByUser(user.get("account"));
        if (byUser != null) {
            return Result.error(400, "账号已存在");
        }
        userService.registerAccount(user.get("account"),user.get("password"));
        return Result.success();
    }
}
