package com.ligg.service.adminweb.impl;

import com.ligg.service.adminweb.SmsMassageService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        
        List<Map<String, String>> resultList = new ArrayList<>();
        
        // 匹配格式：COM数字,号码,[平台名] 内容中的验证码数字
        // 正则表达式解析：
        // (COM\\d+,) - 匹配COM后跟数字和逗号
        // (\\d+), - 匹配手机号码和逗号
        // \\[(.*?)\\] - 匹配方括号中的任意文本（平台名）
        // .*?验证码(\\d{4,6}) - 匹配"验证码"后面的4-6位数字
        Pattern pattern = Pattern.compile("(COM\\d+),(\\d+),\\[(.*?)\\].*?验证码(\\d{4,6})");
        Matcher matcher = pattern.matcher(sms);
        
        while (matcher.find()) {
            String comCode = matcher.group(1);    // COM编码
            String phoneNumber = matcher.group(2); // 手机号码
            String platform = matcher.group(3);    // 平台名称
            String verificationCode = matcher.group(4); // 验证码
            
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("comCode", comCode);
            resultMap.put("phoneNumber", phoneNumber);
            resultMap.put("platform", platform);
            resultMap.put("verificationCode", verificationCode);
            
            resultList.add(resultMap);
            
            System.out.println("提取成功 - 平台: " + platform + ", 号码: " + phoneNumber + ", 验证码: " + verificationCode);
        }
        
        if (resultList.isEmpty()) {
            System.out.println("未匹配到任何验证码信息");
        } else {
            System.out.println("共提取到 " + resultList.size() + " 条验证码信息");
        }
    }
}
