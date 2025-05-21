package com.ligg.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("phone")
public class PhoneEntity {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "phone_id",type = IdType.AUTO)
    private Long phoneId;//id
    private Long phoneNumber;//手机号
    private Integer lineStatus;//线路状态 1.正常、0.异常
    private LocalDateTime registrationTime;//注册时间
    private Integer usageStatus;//使用状态 1.可用、0.不可用
    private Integer regionId;//归属地id，修改名称与新表匹配
    private Long adminUserId;//管理员用户id
    private Float money;//号码价格
}
