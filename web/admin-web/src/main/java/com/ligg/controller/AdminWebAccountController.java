package com.ligg.controller;

import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminWebUserService;
import com.ligg.service.CustomerService;
import com.ligg.service.common.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 后台管理web登录
 */
@RestController
@RequestMapping("/api/adminWeb/account")
public class AdminWebAccountController {

    @Autowired
    private CustomerService userService;

    @Autowired
    private AdminWebUserService adminWebUserService;

    @Autowired
    private TokenService tokenService;


    /**
     * 登录
     * return: token
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody AdminWebUserEntity adminWebUserEntity, HttpServletRequest request) {

       AdminWebUserEntity adminWebInfo = userService.getAdminWebInfo(adminWebUserEntity.getAccount());
       if (adminWebInfo != null){
           boolean verify = BCryptUtil.verify(adminWebUserEntity.getPassword(), adminWebInfo.getPassword());
           if (verify){
               String token = tokenService.createToken(adminWebInfo.getAdminId(), adminWebInfo.getAccount());
                //更新最后登录时间和ip
               adminWebUserService.updateLoginTimeAndIp(adminWebInfo.getAdminId(),request);
               return Result.success(BusinessStates.SUCCESS, token);
           }
           return Result.error(BusinessStates.BAD_REQUEST, "账号或密码错误");
       }
        return Result.error(BusinessStates.BAD_REQUEST, "账号或密码错误");
    }


}
