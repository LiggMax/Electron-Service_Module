package com.ligg.common.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ligg.common.entity.PhoneProjectRelationEntity;
import com.ligg.common.entity.ProjectEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class PhoneAndProjectDto {
    @TableId(value = "phone_id",type = IdType.AUTO)
    private Integer phoneId;//id
    private Long phoneNumber;//手机号
    private Integer lineStatus;//线路状态 1.在线、2.离线
    private LocalDateTime registrationTime;//注册时间
    private Integer usageStatus;//状态
    private List<ProjectEntity> projects;//归属项目，使用嵌套结构
    private String countryCode;//国家编码
    private String regionName;//地区名称
    
    // 地区信息的嵌套对象
    @Data
    public static class RegionInfo {
        private Integer regionId;
        private String regionName;
        private String regionCode;
    }
    
    // 项目信息的完整结构
    @Data
    public static class ProjectInfo {
        private Integer projectId;
        private String projectName;
        private String projectDescription;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
    }
}
