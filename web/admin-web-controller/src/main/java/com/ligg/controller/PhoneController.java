package com.ligg.controller;

import com.ligg.common.entity.PhoneEntity;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.PhoneVo;
import com.ligg.service.common.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 号码
 */
@RestController
@RequestMapping("/api/phone")
public class PhoneController {

    @Autowired
    private PhoneNumberService phoneNumberService;

    /**
     * 号码列表
     */
    @GetMapping
    public Result<List<PhoneVo>> getPhoneList(){
       List<PhoneVo> phoneVoList = phoneNumberService.getPhoneList();
        return Result.success(200,phoneVoList);
    }

    /**
     * 删除号码
     */
    @DeleteMapping()
    public Result<String> deletePhone(Long phoneId){
        phoneNumberService.getBaseMapper().deleteById(phoneId);
        return Result.success(200,"删除成功");
    }
}
