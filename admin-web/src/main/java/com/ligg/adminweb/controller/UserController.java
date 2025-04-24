package com.ligg.adminweb.controller;

import com.ligg.common.utils.Result;
import com.ligg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;
    /**
     * 退出
     */
    @GetMapping("/logout")
    public Result<String> logout() {
        userService.clearToken();
        return Result.success(200,"退出成功");
    }
}
