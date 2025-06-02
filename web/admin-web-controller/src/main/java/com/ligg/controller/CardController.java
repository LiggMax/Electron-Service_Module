package com.ligg.controller;

import com.ligg.common.entity.admin.MerchantEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.AdminMerchantUserService;
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
    private AdminMerchantUserService merchantUserService;

    /**
     * 获取卡商列表
     */
    @GetMapping
    public Result<List<MerchantEntity>> getCardList() {
        return Result.success(200, merchantUserService.getBaseMapper().selectList(null));
    }

    /**
     * 编辑卡商信息
     */
    @PutMapping("/edit")
    public Result<String> updateCardInfo(@Validated @RequestBody MerchantEntity merchantEntity) {
        merchantUserService.updateById(merchantEntity);
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
        MerchantEntity merchantEntity = new MerchantEntity();
        merchantEntity.setAccount(account);
        merchantEntity.setPassword(password);
        merchantEntity.setNickName(nickName);
        merchantEntity.setEmail(email);
        merchantUserService.saveCardUser(merchantEntity);
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
        merchantUserService.resetPassword(userId, password);
        return Result.success(200,"重置成功");
    }

    /**
     * 删除卡商
     */
    @DeleteMapping("/deleteCard")
    public Result<String> deleteCardInfo(@RequestParam Long userId){
        merchantUserService.removeById(userId);
        return Result.success(200,"删除成功");
    }
}
