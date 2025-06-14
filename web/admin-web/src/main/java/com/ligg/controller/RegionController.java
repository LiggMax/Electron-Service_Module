package com.ligg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ligg.common.entity.RegionEntity;
import com.ligg.common.status.BusinessStatus;
import com.ligg.common.utils.Result;
import com.ligg.service.RegionService;
import com.ligg.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 地区管理
 */
@RestController
@RequestMapping("/api/admin_web/region")
public class RegionController {

    @Autowired
    private RegionService regionService;

    @Autowired
    private FileService fileService;

    /**
     * 添加地区
     */
    @PostMapping("/save_or_update_region")
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
        return Result.success(BusinessStatus.SUCCESS, regionService
                .getBaseMapper()
                .selectList(new LambdaQueryWrapper<RegionEntity>()
                        .orderByDesc(RegionEntity::getRegionId)));
    }

    /**
     * 删除地区
     */
    @DeleteMapping
    public Result<String> deleteRegion(@RequestParam Integer regionId) {
        regionService.removeById(regionId);
        return Result.success(BusinessStatus.SUCCESS, "删除成功");
    }

    /**
     * 添加地区图标
     */
    @PostMapping("/upload_region_icon")
    public Result<String> uploadRegionIcon(@RequestParam Integer regionId,
                                           @RequestParam MultipartFile iconFile) {
        if (iconFile.getSize() > 1024 * 1024 * 10) {
            return Result.error(BusinessStatus.BAD_REQUEST, "文件大小不不合法");
        }
        String iconUrl = fileService.uploadImage(iconFile);
        regionService.updateRegionIcon(regionId, iconUrl);
        return Result.success(BusinessStatus.SUCCESS, "上传成功");
    }
}
