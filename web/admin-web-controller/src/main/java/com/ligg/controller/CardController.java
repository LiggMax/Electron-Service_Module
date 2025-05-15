package com.ligg.controller;

import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminUserService;
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
}
