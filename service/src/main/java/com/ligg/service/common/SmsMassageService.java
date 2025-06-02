package com.ligg.service.common;

import java.util.List;
import java.util.Map;

public interface SmsMassageService {
    //  提取验证码和短信
    List<Map<String,String>> extractCodeAndSms(String sms);
    //  保存短信和验证码
    void saveSmsAndCode(List<Map<String, String>> maps);
}
