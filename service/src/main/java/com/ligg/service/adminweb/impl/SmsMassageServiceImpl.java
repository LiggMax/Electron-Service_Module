package com.ligg.service.adminweb.impl;

import com.ligg.common.utils.SmsParserUtil;
import com.ligg.service.adminweb.SmsMassageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SmsMassageServiceImpl implements SmsMassageService {

    /**
     * 提取验证码和短信
     */
    @Override
    public void extractCodeAndSms(String sms) {
        if (sms == null || sms.isEmpty()) {
            System.out.println("短信内容为空");
            return;
        }
        
        // 使用工具类提取验证码信息
        List<Map<String, String>> resultList = SmsParserUtil.extractVerificationCodes(sms);
        
        if (resultList.isEmpty()) {
            System.out.println("未匹配到任何验证码信息");
        } else {
            System.out.println("共提取到 " + resultList.size() + " 条验证码信息");
            for (Map<String, String> result : resultList) {
                System.out.println("提取成功 - 平台: " + result.get("platform") + 
                                  ", 号码: " + result.get("phoneNumber") + 
                                  ", 验证码: " + result.get("verificationCode"));
            }
        }
    }
}
