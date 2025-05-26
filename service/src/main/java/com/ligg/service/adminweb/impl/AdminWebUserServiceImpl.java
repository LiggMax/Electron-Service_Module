package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import com.ligg.common.utils.GetClientIp;
import com.ligg.mapper.AdminWebUserMapper;
import com.ligg.service.adminweb.AdminWebUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AdminWebUserServiceImpl extends ServiceImpl<AdminWebUserMapper, AdminWebUserEntity> implements AdminWebUserService {

    @Autowired
    private AdminWebUserMapper adminWebUserMapper;



    /**
     * 更新登录时间和ip
     */
    @Override
    public void updateLoginTimeAndIp(Long adminId, HttpServletRequest request) {
        String loginIp = GetClientIp.getIp(request);
        log.info("用户:{}登录IP：{}", adminId, loginIp);
        LambdaUpdateWrapper<AdminWebUserEntity> updateWrapper = new LambdaUpdateWrapper<AdminWebUserEntity>()
                .eq(AdminWebUserEntity::getAdminId, adminId)
                .set(AdminWebUserEntity::getLoginIp, loginIp)
                .set(AdminWebUserEntity::getLoginTime, LocalDateTime.now());
        adminWebUserMapper.update(null, updateWrapper);
    }

}
