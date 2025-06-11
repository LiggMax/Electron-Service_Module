package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.RegionEntity;
import com.ligg.mapper.adminweb.RegionMapper;
import com.ligg.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, RegionEntity> implements RegionService {

    @Autowired
    private RegionMapper regionMapper;

    /**
     * 更新区域图标
     */
    @Override
    public void updateRegionIcon(Integer regionId, String iconUrl) {
        regionMapper.update(new LambdaUpdateWrapper<RegionEntity>()
                .eq(RegionEntity::getRegionId, regionId)
                .set(RegionEntity::getIcon, iconUrl));
    }
}
