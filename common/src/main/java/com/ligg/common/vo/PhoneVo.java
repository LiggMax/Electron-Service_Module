package com.ligg.common.vo;

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
public class PhoneVo {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long phoneId;//id
    private Long phoneNumber;//手机号
    private LocalDateTime registrationTime;//注册时间
    private Integer usageStatus;//使用状态 1.可用、0.被购买
    private String regionName;//地区名称
    private String projectName;//项目名称
    private String admin_nick_name;//卡商昵称
    private Float money;//号码价格
}
