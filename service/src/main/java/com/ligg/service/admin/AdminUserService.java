package com.ligg.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.admin.AdminUserEntity;
import com.ligg.common.vo.OrderVo;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public interface AdminUserService extends IService<AdminUserEntity> {
    //重置密码
    void resetPassword(Long userId, @Min(value = 6,message = "密码长度不能小于6位") @Max(value = 20,message = "密码长度不能大于20位") String password);
    //添加卡商
    void saveCardUser(AdminUserEntity adminUserEntity);
    //更新登录时间
    void updateLoginTime(Long userId);
    //获取订单
    List<OrderVo> getOrder(Long AdminId);
}
