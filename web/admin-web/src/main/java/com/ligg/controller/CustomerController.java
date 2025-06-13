package com.ligg.controller;

import com.ligg.common.entity.user.UserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.CustomerManageService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 客户管理
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerManageService customerManageService;

    /**
     * 客户列表
     */
    @GetMapping("/user")
    public Result<List<UserEntity>> getUserList() {
        List<UserEntity> userEntities = customerManageService.getBaseMapper().selectList(null);
        return Result.success(200, userEntities);
    }

    /**
     * 更新客户状态
     */
    @PutMapping("/status")
    public Result<String> updateUserStatus(@RequestParam Long userId, @RequestParam Boolean status) {
        customerManageService.updateUserStatus(userId, status);
        return Result.success(200, "更新成功");
    }

    /**
     * 编辑客户个人信息
     */
    //TODO 接口有被修改客户金融的风险 需要修改
    @PutMapping("/edit")
    public Result<String> updateUserInfo(@Validated @RequestBody UserEntity userEntity) {
        customerManageService.updateCustomerInfoById(userEntity);
        return Result.success(200, "更新成功");
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset")
    public Result<String> resetPassword(@RequestParam Long userId,
                                        @RequestParam
                                        @Min(value = 6, message = "密码长度不能小于6位")
                                        @Max(value = 16, message = "密码长度不能超过16位")
                                        String password) {
        customerManageService.updatePassword(userId, password);
        return Result.success(200, "重置成功");
    }

    /**
     * 添加客户
     */
    @PostMapping("/addUser")
    public Result<String> addUser(@Pattern(regexp = "^[a-zA-Z\\d]{6,20}$") String account,
                                  @Pattern(regexp = "^[a-zA-Z\\d]{6,20}$") String password,
                                  @Pattern(regexp = "^[a-zA-Z\\d]{1,20}$") String nickName,
                                  @Email String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAccount(account);
        userEntity.setPassword(password);
        userEntity.setNickName(nickName);
        userEntity.setEmail(email);
        userEntity.setInvitationCode(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        customerManageService.saveUser(userEntity);
        return Result.success(200, "添加成功");
    }

    /**
     * 删除客户
     */
    @DeleteMapping("/deleteUser")
    public Result<String> deleteUser(@RequestParam Long userId) {
        customerManageService.removeById(userId);
        return Result.success(200, "删除成功");
    }

    /**
     * 客户余额编辑
     */
    @PutMapping("/edit_balance")
    public Result<String> editBalance(@RequestParam Long userId,
                                      @RequestParam Float balance,
                                      @RequestParam Boolean isType) {
        customerManageService.updateBalance(userId, balance, isType);
        return Result.success(200, "编辑成功");
    }
}
