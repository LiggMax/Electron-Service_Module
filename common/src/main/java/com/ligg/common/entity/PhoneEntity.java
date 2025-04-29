package com.ligg.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneEntity {
    private Integer phoneId;//id
    private Long phoneNumber;//手机号
    private Integer phoneProjectId;//项目id
    private Integer lineStatus;//线路状态 1.在线、2.离线
    private String countryCode;//号码归属地
    private LocalDateTime registrationTime;//注册时间
    private Integer usageStatus;//状态
    private Integer phoneRegionId;//号码归属地区id
}
