package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.InvitationRelationsService;
import com.ligg.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 账户接口
 */
@Slf4j
@RestController
@RequestMapping("/api/user/account")
public class UserAccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private InvitationRelationsService invitationRelationsService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<String> login(@RequestBody Map<String, String> request) {
        String account = request.get("account");
        String password = request.get("password");

        UserEntity byAccount = userService.findAccountAndPasswordByUser(account);
        if (byAccount == null || !BCryptUtil.verify(password, byAccount.getPassword())) {
            log.warn("登录失败：账号 {}", account);
            return Result.error(400, "账号或密码错误");
        }

        if (byAccount.getUserStatus() == 0) {
            return Result.error(400, "该账号已被注销");
        }

        String token = userService.createToken(byAccount.getUserId(), byAccount.getAccount());
        //更新登录时间
        userService.updateLoginTime(byAccount.getUserId());
        return Result.success(200, token);
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
        String account = user.get("account");
        userService.registerAccount(account, user.get("password"));

        //添加邀请关系
        if (user.get("invitationCode") != null) {
            UserEntity userEntity = userService.getBaseMapper()
                    .selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getAccount, account));
            if (userEntity != null) {
                invitationRelationsService.addInvitationRelations(user.get("invitationCode"),userEntity.getUserId());
            }
        }
        return Result.success();
    }
}
