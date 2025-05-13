package com.ligg.controller;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 卡商
 */
@RestController
@RequestMapping("/api/Card")
public class CardController {

    @Autowired
    private AdminUserService adminUserService;

    @GetMapping
    public Result<List<AdminUserEntity>> getCardList() {
        return Result.success(200,adminUserService.getBaseMapper().selectList(null));
    }
}
