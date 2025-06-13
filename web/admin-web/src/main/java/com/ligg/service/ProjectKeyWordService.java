package com.ligg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ligg.common.entity.adminweb.ProjectKeyWordEntity;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/13
 **/
public interface ProjectKeyWordService extends IService<ProjectKeyWordEntity> {

    /**
     * 保存关键词
     */
    void saveKeyWord(ProjectKeyWordEntity projectKeyWord);

    /**
     * 根据项目id查询关键词
     */
    List<ProjectKeyWordEntity> getKeyWordByProjectId(Integer projectId);
}
