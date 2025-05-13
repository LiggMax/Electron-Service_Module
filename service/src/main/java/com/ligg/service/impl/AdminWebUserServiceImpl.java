package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.AdminWebUserEntity;
import com.ligg.mapper.AdminWebUserMapper;
import com.ligg.service.AdminWebUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminWebUserServiceImpl extends ServiceImpl<AdminWebUserMapper, AdminWebUserEntity> implements AdminWebUserService {
}
