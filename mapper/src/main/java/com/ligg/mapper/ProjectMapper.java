package com.ligg.mapper;

import com.ligg.common.dto.ProjectListDto;
import com.ligg.common.entity.PhoneEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProjectMapper {

    /**
     * 获取项目数量
     * @param projectName 项目名称
     * @return 数量
     */
    Integer getPhoneCountByProject(String projectName);

    /**
     * 获取所有项目列表及数量
     * @return 项目列表
     */
    List<ProjectListDto> getAllProjects();

    /**
     * 根据项目名称查询项目数量
     */
    Integer getProjectCountByName(String projectName);

    /**
     * 根据项目名称查询手机号列表
     * @param projectNames 项目名称数组
     * @return 手机号列表
     */
    List<PhoneEntity> getPhonesByProject(@Param("projectNames") String[] projectNames);
}
