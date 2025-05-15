package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.AdminUserEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public interface AdminUserService extends IService<AdminUserEntity> {
    //重置密码
    void resetPassword(Long userId, @Min(value = 6,message = "密码长度不能小于6位") @Max(value = 20,message = "密码长度不能大于20位") String password);
    //添加卡商
    void saveCardUser(AdminUserEntity adminUserEntity);
    //更新登录时间
    void updateLoginTime(Long userId);
}
