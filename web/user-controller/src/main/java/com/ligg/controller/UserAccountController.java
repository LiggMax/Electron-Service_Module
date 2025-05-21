package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.common.utils.Result;
import com.ligg.service.common.InvitationRelationsService;
import com.ligg.service.user.UserService;
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
        String account = user.get("account");
        String password = user.get("password");
        String invitationCode = user.get("invitationCode");

        // 校验账号和密码长度
        if (account.length() < 6 || password.length() < 6) {
            return Result.error(400, "账号或密码长度不小于6位");
        }
        if (account.length() > 20 || password.length() > 20) {
            return Result.error(400, "账号或密码长度不大于20位");
        }

        // 检查账号是否已存在
        if (userService.findByUser(account) != null) {
            return Result.error(400, "账号已存在");
        }

        // 注册账户
        userService.registerAccount(account, password);

        // 处理邀请码逻辑
        if (invitationCode != null && !invitationCode.isEmpty()) {
            UserEntity inviterAccount = userService.getBaseMapper()
                    .selectOne(new LambdaQueryWrapper<UserEntity>()
                            .eq(UserEntity::getInvitationCode, invitationCode));
            if (inviterAccount == null) {
                return Result.error(400, "邀请码无效");
            }

            // 添加邀请关系
            invitationRelationsService.addInvitationRelations(invitationCode, inviterAccount.getAccount(), account);
        }

        return Result.success();
    }
}
