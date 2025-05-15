package com.ligg.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.adminweb.CustomerService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户管理
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService userManagementService;
    @Autowired
    private CustomerService customerService;

    /**
     * 客户列表
     */
    @GetMapping("/user")
    public Result<List<UserEntity>> getUserList(){
        List<UserEntity> userEntities = userManagementService.getBaseMapper().selectList(null);
        return Result.success(200,userEntities);
    }

    /**
     * 更新客户状态
     */
    @PutMapping("/status")
    public Result<String> updateUserStatus(@RequestParam Long userId,@RequestParam Boolean status ){
        userManagementService.updateUserStatus(userId,status);
        return Result.success(200,"更新成功");
    }

    /**
     * 编辑客户个人信息
     */
    @PutMapping("/edit")
    public Result<String> updateUserInfo(@Validated @RequestBody UserEntity userEntity){
        userManagementService.updateById(userEntity);
        return Result.success(200,"更新成功");
    }

    /**
     * 重置密码
     */
    @PutMapping("/reset")
    public Result<String> resetPassword(@RequestParam Long userId,
                                        @RequestParam
                                        @Min(value = 6,message = "密码长度不能小于6位")
                                        @Max(value = 16,message = "密码长度不能超过16位")
                                        String password){
        userManagementService.updatePassword(userId,password);
        return Result.success(200,"重置成功");
    }

    /**
     * 添加客户
     */
    @PostMapping("/addUser")
    public Result<String> addUser(@RequestBody UserEntity userEntity){
        customerService.saveUser(userEntity);
        return Result.success(200,"添加成功");
    }
}
