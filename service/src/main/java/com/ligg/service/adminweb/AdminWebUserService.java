package com.ligg.service.adminweb;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.adminweb.AdminWebUserEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface AdminWebUserService extends IService<AdminWebUserEntity> {
    //跟新登录时间和ip
    void updateLoginTimeAndIp(Long adminId,HttpServletRequest request);
}
