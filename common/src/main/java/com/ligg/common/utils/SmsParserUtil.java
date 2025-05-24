package com.ligg.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信解析工具类
 * 用于从各种格式的短信中提取手机号码和短信内容
 */
public class SmsParserUtil {

    /**
     * 从短信内容中提取短信信息
     *
     * @param sms 短信内容字符串
     * @return 包含短信信息的列表
     */
    public static List<Map<String, String>> extractSmsContent(String sms) {
        if (sms == null || sms.isEmpty()) {
            return new ArrayList<>();
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        // 优化分割逻辑，兼容多种短信开头
        String[] smsArray = sms.split("(?=COM\\d+,[0-9]{6,15},)");
        for (String smsContent : smsArray) {
            smsContent = smsContent.trim();
            if (smsContent.isEmpty()) continue;
            Map<String, String> result = extractSingleSms(smsContent);
            if (result != null) {
                resultList.add(result);
            } else {
                // 无法解析的短信，返回整条内容
                Map<String, String> fallback = new HashMap<>();
                // 尝试提取号码
                Pattern phonePattern = Pattern.compile("(\\d{6,15})");
                Matcher phoneMatcher = phonePattern.matcher(smsContent);
                String phoneNumber = phoneMatcher.find() ? phoneMatcher.group(1) : "未知";
                fallback.put("comCode", "未知");
                fallback.put("phoneNumber", phoneNumber);
                fallback.put("messageContent", smsContent);
                fallback.put("platform", "未知平台");
                resultList.add(fallback);
            }
        }
        return resultList;
    }
    
    /**
     * 提取单条短信中的信息
     *
     * @param smsContent 单条短信内容
     * @return 包含COM编码、手机号码、平台名称和短信内容的Map
     */
    private static Map<String, String> extractSingleSms(String smsContent) {
        // 优化正则，兼容号码为8位及以上，允许逗号、空格等分隔
        Pattern comPattern = Pattern.compile("(COM\\d+)[,，](\\d{6,15})[,，](.*)");
        Matcher comMatcher = comPattern.matcher(smsContent);
        if (!comMatcher.find()) {
            // 兼容部分短信格式如"新短信!(号码)号码为[xxx]内容[...]"
            Pattern altPattern = Pattern.compile("新短信!\\((\\d{6,15})\\).*内容\\[(.*?)]");
            Matcher altMatcher = altPattern.matcher(smsContent);
            if (altMatcher.find()) {
                String phoneNumber = altMatcher.group(1);
                String content = altMatcher.group(2);
                Map<String, String> result = new HashMap<>();
                result.put("comCode", "未知");
                result.put("phoneNumber", phoneNumber);
                result.put("messageContent", content);
                String platform = extractPlatform(content);
                result.put("platform", platform != null ? platform : "未知平台");
                return result;
            }
            return null;
        }
        String comCode = comMatcher.group(1);
        String phoneNumber = comMatcher.group(2);
        String content = comMatcher.group(3);
        
        Map<String, String> result = new HashMap<>();
        result.put("comCode", comCode);
        result.put("phoneNumber", phoneNumber);
        result.put("messageContent", content);
        String platform = extractPlatform(content);
        result.put("platform", platform != null ? platform : "未知平台");
        return result;
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