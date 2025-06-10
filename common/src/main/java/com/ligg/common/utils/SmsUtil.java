package com.ligg.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Ligg
 * @Time 2025/6/10
 * <p>
 * 短信解析工具类
 **/
public class SmsUtil {

    /**
     * 从短信解析出号码、验证码
     */
    public static List<Map<String, String>> extractSmsContent(String sms) {
        if (sms == null || sms.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, String>> resultList = new ArrayList<>();

        // 正则表达式匹配 COM数字,号码, 的模式
        Pattern pattern = Pattern.compile("COM(\\d+),(\\d+),");
        Matcher matcher = pattern.matcher(sms);

        while (matcher.find()) {
            Map<String, String> smsInfo = new HashMap<>();
            String phoneNumber = matcher.group(2);       // 号码
            smsInfo.put("phoneNumber", phoneNumber);
            resultList.add(smsInfo);
        }

        return resultList;
    }
}
