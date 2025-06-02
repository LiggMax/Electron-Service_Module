package com.ligg.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.RegionEntity;
import com.ligg.mapper.AdminWeb.RegionMapper;
import com.ligg.service.RegionService;
import org.springframework.stereotype.Service;


@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {

}
