package com.ligg.controller;

import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.MerchantUserService;
import com.ligg.service.common.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 卡商登录
 */
@RestController
@RequestMapping("/api/merchant/account")
public class MerchantAccountController {


    @Autowired
    private MerchantUserService merchantUserService;

    @Autowired
    private TokenService tokenService;
    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<?> login(@RequestParam String account,
                           @RequestParam String password){
        MerchantEntity userInfo = merchantUserService.getUserByAccount(account);
        if (userInfo == null){
            return Result.error(BusinessStates.BAD_REQUEST, "账号或密码错误");
        }
        if (!BCryptUtil.verify(password, userInfo.getPassword())){
            return Result.error(BusinessStates.BAD_REQUEST, "账号或密码错误");
        }
        String token = tokenService.createToken(userInfo.getUserId(), userInfo.getAccount());
        //更新登录时间
        merchantUserService.updateLoginTime(userInfo.getUserId());
        return Result.success(BusinessStates.SUCCESS, token);
    }


}
