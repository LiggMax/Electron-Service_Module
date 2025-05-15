package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AdminUserEntity;
import com.ligg.common.utils.BCryptUtil;
import com.ligg.mapper.AdminUserMapper;
import com.ligg.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUserEntity> implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    /**
     * 重置密码
     */
    @Override
    public void resetPassword(Long userId, String password) {
        LambdaUpdateWrapper<AdminUserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AdminUserEntity ::getUserId, userId)
                .set(AdminUserEntity::getPassword, BCryptUtil.encrypt(password));
        adminUserMapper.update(null, updateWrapper);
    }
}
