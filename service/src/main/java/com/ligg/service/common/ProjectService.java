package com.ligg.service.common;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.dto.RegionCommodityDto;
import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.entity.ProjectEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 项目服务接口
 */
public interface ProjectService extends IService<ProjectEntity> {
    
    /**
     * 根据项目名称查询手机号列表
     * @param projectNames 项目名称数组
     * @return 手机号列表
     */
    List<PhoneEntity> getPhonesByProject(String[] projectNames);

    /**
     * 获取所有项目列表
     */
    List<ProjectListDto> getAllProjectsList();

    /**
     * 获取指定项目商品列表
     */
    List<RegionCommodityDto> getProjectCommodityList(Long projectId);

    /**
     * 获取项目和地区列表
     */
    Map<String, Object> getProjectAndRegionList();
    
    /**
     * 根据项目名称查询项目ID
     * @param projectName 项目名称
     * @return 项目ID，如果不存在则返回null
     */
    Integer getProjectIdByName(String projectName);
    
    // 新增项目
    void saveProject(ProjectEntity projectEntity);

    // 更新项目
    void updateProject(ProjectEntity projectEntity);

    //上传项目图标
    void uploadIcon(@NotNull Integer projectId, String iconUrl);
}
