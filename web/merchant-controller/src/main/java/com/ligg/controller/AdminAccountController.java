package com.ligg.controller;

import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.merchant.MerchantUserService;
import com.ligg.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 卡商登录
 */
@RestController
@RequestMapping("/api/admin/account")
public class AdminAccountController {

    @Autowired
    private CustomerService userService;
    @Autowired
    private MerchantUserService merchantUserService;
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestParam String account,
                           @RequestParam String password){
        MerchantEntity userInfo = userService.findByAdminUser(account);
        if (userInfo == null){
            return Result.error(400,"账号或密码错误");
        }
        if (!BCryptUtil.verify(password, userInfo.getPassword())){
            return Result.error(400,"账号或密码错误");
        }
        String token = userService.createToken(userInfo.getUserId(),userInfo.getAccount());
        //更新登录时间
        merchantUserService.updateLoginTime(userInfo.getUserId());
        return Result.success(200,token);
    }


}
