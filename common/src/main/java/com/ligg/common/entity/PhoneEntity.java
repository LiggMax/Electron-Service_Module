package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("phone")
public class PhoneEntity {
    @TableId(value = "phone_id",type = IdType.AUTO)
    private Integer phoneId;//id
    private Long phoneNumber;//手机号
    private Integer lineStatus;//线路状态 1.在线、2.离线
    private LocalDateTime registrationTime;//注册时间
    private Integer usageStatus;//状态
    private Integer phoneProjectId;//项目id
    private Long adminUserId;//号码归属卡商id
    private Integer phoneRegionId;//归属地id
}
