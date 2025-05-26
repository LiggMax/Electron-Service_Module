package com.ligg.controller;

import com.ligg.common.entity.RegionEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.adminweb.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地区管理
 */
@RestController
@RequestMapping("/api/adminWeb/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 添加地区
     */
    @PostMapping("/saveOrUpdateRegion")
    public Result<String> addRegion(@RequestBody RegionEntity regionEntity) {
        //添加或修改
        regionEntity.setRegionCreatedAt(LocalDateTime.now());
        regionService.saveOrUpdate(regionEntity);
        return Result.success();
    }

    /**
     * 获取地区列表
     */
    @GetMapping
    public Result<List<RegionEntity>> getRegionList() {
        return Result.success(200, regionService
                .getBaseMapper()
                .selectList(null));
    }

    /**
     * 删除地区
     */
    @DeleteMapping
    public Result<String> deleteRegion(@RequestParam Integer regionId) {
        regionService.removeById(regionId);
        return Result.success(200, "删除成功");
    }
}
