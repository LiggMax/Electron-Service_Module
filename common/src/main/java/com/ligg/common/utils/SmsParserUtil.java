package com.ligg.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信解析工具类
 * 用于从各种格式的短信中提取验证码、手机号码和平台信息
 */
public class SmsParserUtil {

    /**
     * 从短信内容中提取所有验证码信息
     *
     * @param sms 短信内容字符串
     * @return 包含验证码信息的列表
     */
    public static List<Map<String, String>> extractVerificationCodes(String sms) {
        if (sms == null || sms.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Map<String, String>> resultList = new ArrayList<>();
        
        // 分割多条短信内容
        String[] smsArray = sms.split("COM", -1);
        
        for (int i = 1; i < smsArray.length; i++) { // 从1开始，因为第0个是空字符串
            String smsContent = "COM" + smsArray[i];
            Map<String, String> result = extractSingleSms(smsContent);
            if (result != null) {
                resultList.add(result);
            }
        }
        
        return resultList;
    }
    
    /**
     * 提取单条短信中的信息
     *
     * @param smsContent 单条短信内容
     * @return 包含COM编码、手机号码、平台名称和验证码的Map
     */
    private static Map<String, String> extractSingleSms(String smsContent) {
        // 首先提取COM编码和手机号码
        Pattern comPattern = Pattern.compile("(COM\\d+),(\\d+),(.*)");
        Matcher comMatcher = comPattern.matcher(smsContent);
        
        if (!comMatcher.find()) {
            return null;
        }
        
        String comCode = comMatcher.group(1);
        String phoneNumber = comMatcher.group(2);
        String content = comMatcher.group(3);
        
        // 提取验证码
        String verificationCode = extractVerificationCode(content);
        
        // 如果找到验证码，创建结果对象
        if (verificationCode != null) {
            Map<String, String> result = new HashMap<>();
            result.put("comCode", comCode);
            result.put("phoneNumber", phoneNumber);
            result.put("verificationCode", verificationCode);
            
            // 提取平台名称
            String platform = extractPlatform(content);
            result.put("platform", platform != null ? platform : "未知平台");
            
            return result;
        }
        
        return null;
    }
    
    /**
     * 从短信内容中提取验证码
     *
     * @param content 短信内容
     * @return 验证码字符串，未找到则返回null
     */
    public static String extractVerificationCode(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        
        String verificationCode = null;
        
        // 匹配模式1: 验证码：123456 或 验证码:123456
        Pattern pattern1 = Pattern.compile("[驗證碼验证码][:：]\\s*(\\d{4,6})");
        Matcher matcher1 = pattern1.matcher(content);
        if (matcher1.find()) {
            return matcher1.group(1);
        }
        
        // 匹配模式2: code is 123456
        Pattern pattern2 = Pattern.compile("code is (\\d{4,6})");
        Matcher matcher2 = pattern2.matcher(content);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        
        // 匹配模式3: 直接匹配数字 (适用于 "1234 is your verification code")
        Pattern pattern3 = Pattern.compile("(\\d{4,6}) is your verification code");
        Matcher matcher3 = pattern3.matcher(content);
        if (matcher3.find()) {
            return matcher3.group(1);
        }
        
        // 匹配模式4: 英文短信中的验证码
        Pattern pattern4 = Pattern.compile("verification code is (\\d{4,6})");
        Matcher matcher4 = pattern4.matcher(content);
        if (matcher4.find()) {
            return matcher4.group(1);
        }
        
        // 如果以上都没匹配到，尝试匹配任何4-6位数字
        Pattern pattern5 = Pattern.compile("\\b(\\d{4,6})\\b");
        Matcher matcher5 = pattern5.matcher(content);
        if (matcher5.find()) {
            return matcher5.group(1);
        }
        
        return null;
    }
    
    /**
     * 从短信内容中提取平台名称
     *
     * @param content 短信内容
     * @return 平台名称，未找到则返回null
     */
    public static String extractPlatform(String content) {
        if (content == null || content.isEmpty()) {
            return null;
        }
        
        // 尝试匹配方括号中的内容 [抖音]
        Pattern pattern1 = Pattern.compile("\\[(.*?)\\]");
        Matcher matcher1 = pattern1.matcher(content);
        if (matcher1.find()) {
            return matcher1.group(1);
        }
        
        // 尝试匹配【】中的内容 【京东】
        Pattern pattern2 = Pattern.compile("【(.*?)】");
        Matcher matcher2 = pattern2.matcher(content);
        if (matcher2.find()) {
            return matcher2.group(1);
        }
        
        // 匹配小红书平台
        if (content.startsWith("Red")) {
            return "小红书";
        }
        
        return null;
    }
} 