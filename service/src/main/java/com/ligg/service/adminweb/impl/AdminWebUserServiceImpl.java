package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AdminWebUserEntity;
import com.ligg.mapper.AdminWebUserMapper;
import com.ligg.service.adminweb.AdminWebUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminWebUserServiceImpl extends ServiceImpl<AdminWebUserMapper, AdminWebUserEntity> implements AdminWebUserService {
}
