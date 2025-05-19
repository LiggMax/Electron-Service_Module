package com.ligg.service.adminweb.impl;

import com.ligg.service.adminweb.SmsMassageService;
import org.springframework.stereotype.Service;

@Service
public class SmsMassageServiceImpl implements SmsMassageService {

    /**
     * 提取验证码和短信
x     */
    @Override
    public void extractCodeAndSms(String sms) {

        System.out.println(sms);
    }
}
