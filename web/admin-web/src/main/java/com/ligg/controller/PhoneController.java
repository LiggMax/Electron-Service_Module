package com.ligg.controller;

import com.ligg.common.statuEnum.BusinessStates;
import com.ligg.common.utils.Result;
import com.ligg.common.vo.PageVo;
import com.ligg.common.vo.PhoneVo;
import com.ligg.service.common.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result<PageVo<PhoneVo>> getPhoneList(@RequestParam(required = false) Long phoneNumber,
                                                Long pageNum,
                                                Long pageSize
    ) {
        PageVo<PhoneVo> phoneVoList = phoneNumberService.getPhoneList(phoneNumber, pageNum, pageSize);
        return Result.success(BusinessStates.SUCCESS, phoneVoList);
    }

    /**
     * 删除号码
     */
    @DeleteMapping()
    public Result<String> deletePhone(Long phoneId) {
        phoneNumberService.getBaseMapper().deleteById(phoneId);
        return Result.success(BusinessStates.SUCCESS, "删除成功");
    }
}
