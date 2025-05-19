package com.ligg.service.adminweb;

import org.springframework.stereotype.Service;

public interface SmsMassageService {
    //  提取验证码和短信
    void extractCodeAndSms(String sms);
}
