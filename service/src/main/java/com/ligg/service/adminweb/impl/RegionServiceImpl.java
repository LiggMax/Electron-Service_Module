package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.RegionEntity;
import com.ligg.mapper.AdminWeb.RegionMapper;
import com.ligg.service.adminweb.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {

    @Autowired
    private RegionMapper regionMapper;


}
