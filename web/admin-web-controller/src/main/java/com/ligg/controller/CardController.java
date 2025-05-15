package com.ligg.controller;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminUserService;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 卡商
 */
@RestController
@RequestMapping("/api/card")
public class CardController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取卡商列表
     */
    @GetMapping
    public Result<List<AdminUserEntity>> getCardList() {
        return Result.success(200,adminUserService.getBaseMapper().selectList(null));
    }

    /**
     * 编辑卡商信息
     */
    @PutMapping("/edit")
    public Result<String> updateCardInfo(@Validated @RequestBody AdminUserEntity adminUserEntity) {
        adminUserService.updateById(adminUserEntity);
        return Result.success(200,"修改成功");
    }

    /**
     * 添加卡商
     */
    @PostMapping("/add")
    public Result<String> addCardInfo(@Pattern(regexp = "^[a-zA-Z\\d]{6,20}$") String account,
                                      @Pattern(regexp = "^[a-zA-Z\\d]{6,20}$") String password,
                                      @Pattern(regexp = "^[a-zA-Z\\d]{1,20}$") String nickName,
                                      @Email String email) {
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        adminUserEntity.setAccount(account);
        adminUserEntity.setPassword(password);
        adminUserEntity.setNickName(nickName);
        adminUserEntity.setEmail(email);
        adminUserService.saveCardUser(adminUserEntity);
        return Result.success(200,"添加成功");
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset")
    public Result<String> resetPassword(@RequestParam Long userId,
                                        @Min(value = 6,message = "密码长度不能小于6位")
                                        @Max (value = 20,message = "密码长度不能大于20位")
                                        @RequestParam String password){
        adminUserService.resetPassword(userId,password);
        return Result.success(200,"重置成功");
    }
}
