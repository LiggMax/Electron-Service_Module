package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.RegionEntity;


public interface RegionService extends IService<RegionEntity> {
    /**
     * 更新区域图标
     */
    void updateRegionIcon(Integer regionId, String iconUrl);
}
