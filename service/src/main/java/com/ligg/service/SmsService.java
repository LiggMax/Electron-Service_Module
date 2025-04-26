package com.ligg.service;

import com.ligg.common.dto.SmsDto;

import java.util.List;

public interface SmsService {

    //获取短信列表
    List<SmsDto> getSmsList(Long userId);
}
