package com.ligg.controller;

import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.utils.Result;
import com.ligg.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * 号码列表
     */
    @GetMapping
    public Result<List<PhoneEntity>> getPhoneList(){
        return Result.success(200,phoneNumberService.getBaseMapper().selectList(null));
    }
}
