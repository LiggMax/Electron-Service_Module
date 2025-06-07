package com.ligg.service.common;

import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;

import java.util.List;

public interface SmsService {

    //获取号码列表
    List<SmsDto> getSmsList(Long userId);

    //获取验证码列表
    List<CodeVo> getCodeList(Long userId);
}
