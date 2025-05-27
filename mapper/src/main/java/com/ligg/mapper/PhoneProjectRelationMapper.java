package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.PhoneProjectRelationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PhoneProjectRelationMapper extends BaseMapper<PhoneProjectRelationEntity> {

    //  更新手机项目关系表
    void updateAvailableStatus(Long phoneId, Integer projectId);
}
