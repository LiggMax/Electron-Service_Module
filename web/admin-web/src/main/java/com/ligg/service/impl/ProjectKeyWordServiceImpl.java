package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ligg.common.entity.adminweb.ProjectKeyWordEntity;
import com.ligg.mapper.adminweb.ProjectKeyWordMapper;
import com.ligg.service.ProjectKeyWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/13
 **/
@Service
public class ProjectKeyWordServiceImpl extends ServiceImpl<ProjectKeyWordMapper, ProjectKeyWordEntity> implements ProjectKeyWordService {

    @Autowired
    private ProjectKeyWordMapper projectKeyWordMapper;

    /**
     * 保存关键字
     */
    @Override
    public void saveKeyWord(ProjectKeyWordEntity projectKeyWord) {
        projectKeyWord.setUpdateAt(LocalDateTime.now());
        projectKeyWordMapper.insert(projectKeyWord);
    }

    /**
     * 获取关键茨
     */
    @Override
    public List<ProjectKeyWordEntity> getKeyWordByProjectId(Integer projectId) {
        return projectKeyWordMapper.selectList(new LambdaQueryWrapper<ProjectKeyWordEntity>()
                .eq(ProjectKeyWordEntity::getProjectId, projectId)
                .orderByDesc(ProjectKeyWordEntity::getUpdateAt));
    }
}
