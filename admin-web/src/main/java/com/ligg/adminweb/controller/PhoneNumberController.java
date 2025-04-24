package com.ligg.adminweb.controller;

import com.ligg.common.entity.Phone;
import com.ligg.common.utils.Result;
import com.ligg.service.PhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/admin/phone")
public class PhoneNumberController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * 条件查询卡号数据
     */
    @GetMapping("/list")
    public Result<List<Phone>> phoneList(
            @RequestParam(required = false) String keyword ,//关键字
            @RequestParam(required = false) String countryCode,//号码归属地
            @RequestParam(required = false) Integer usageStatus //号码状态
            ){
        List<Phone> phoneList = phoneNumberService.phoneList(countryCode, usageStatus, keyword);
        return  Result.success(200,phoneList);
    }

    /**
     * 查询手机号详情
     */
    @GetMapping("/phoneDetail")
    public Result<?> phoneDetail(Integer phoneId){
        // 调用服务获取手机号详情（包含基本信息和项目列表）
        Map<String, Object> phoneDetailData = phoneNumberService.phoneDetail(phoneId);
        return Result.success(200, phoneDetailData);
    }
}
