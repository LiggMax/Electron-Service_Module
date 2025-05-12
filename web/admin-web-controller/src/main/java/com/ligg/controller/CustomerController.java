package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.UserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminUserService;
import com.ligg.service.CustomerService;
import com.ligg.service.ProjectService;
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

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private ProjectService projectService;

    /**
     * 客户列表
     */
    @GetMapping("/user")
    public Result<List<UserEntity>> getUserList(){
        List<UserEntity> userEntities = userManagementService.getBaseMapper().selectList(null);
        return Result.success(200,userEntities);
    }

    /**
     * 卡商列表
     */
    @GetMapping("/adminUser")
    public Result<List<AdminUserEntity>> getAdminUserList(){
        return Result.success(200,adminUserService.getBaseMapper().selectList(null));
    }

    /**
     * 项目列表
     */
    @GetMapping("/project")
    public Result<List<ProjectEntity>> getProjectList(){
        return Result.success(200,projectService.getBaseMapper().selectList(null));
    }
}
