package com.ligg.service.impl;

import com.ligg.common.dto.SmsDto;
import com.ligg.mapper.SmsMapper;
import com.ligg.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;
    @Override
    public List<SmsDto> getSmsList(Long userId) {
        return smsMapper.getSmsList(userId);
    }
}
