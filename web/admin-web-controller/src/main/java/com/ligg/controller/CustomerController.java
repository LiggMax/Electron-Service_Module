package com.ligg.controller;

import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService userManagementService;

    /**
     * 客户列表
     */
    @GetMapping("/user")
    public Result<List<UserEntity>> getUserList(){
        List<UserEntity> userEntities = userManagementService.getBaseMapper().selectList(null);
        return Result.success(200,userEntities);
    }
}
