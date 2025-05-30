package com.ligg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.PhoneProjectRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PhoneProjectRelationMapper extends BaseMapper<PhoneProjectRelationEntity> {

    /**
     * 更新手机号项目关联状态为不可用
     */
    int updateAvailableStatus(@Param("phoneId") Long phoneId, @Param("projectId") Integer projectId);

    /**
     * 检查号码对指定项目是否可用
     */
    int checkPhoneProjectAvailable(@Param("phoneId") Long phoneId, @Param("projectId") Integer projectId);

    /**
     * 有条件的更新号码项目关联状态（只有当前状态为可用时才更新）
     */
    int updateAvailableStatusWithCondition(@Param("phoneId") Long phoneId, @Param("projectId") Integer projectId);

    /**
     * 回滚号码项目关联状态为可用
     */
    int rollbackAvailableStatus(@Param("phoneId") Long phoneId, @Param("projectId") Integer projectId);
}
