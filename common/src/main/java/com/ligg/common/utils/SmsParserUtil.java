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
        // 优化分割逻辑，兼容多种短信开头
        // 修正正则表达式，括号补全，兼容多条短信分割
        String[] smsArray = sms.split("(?=COM\\d+,[0-9]{6,15},)");
        for (String smsContent : smsArray) {
            smsContent = smsContent.trim();
            if (smsContent.isEmpty()) continue;
            Map<String, String> result = extractSingleSms(smsContent);
            if (result != null) {
                resultList.add(result);
            } else {
                // 未能提取验证码时，返回整条短信内容和号码
                Map<String, String> fallback = new HashMap<>();
                // 尝试提取号码
                Pattern phonePattern = Pattern.compile("(\\d{6,15})");
                Matcher phoneMatcher = phonePattern.matcher(smsContent);
                String phoneNumber = phoneMatcher.find() ? phoneMatcher.group(1) : "未知";
                fallback.put("comCode", "未知");
                fallback.put("phoneNumber", phoneNumber);
                // 若号码成功但验证码未提取到，则将整条内容放入验证码字段
                if (!"未知".equals(phoneNumber)) {
                    fallback.put("verificationCode", smsContent);
                } else {
                    fallback.put("verificationCode", "未提取到验证码");
                }
                fallback.put("platform", "未知平台");
                fallback.put("rawContent", smsContent);
                resultList.add(fallback);
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
        // 优化正则，兼容号码为8位及以上，允许逗号、空格等分隔
        Pattern comPattern = Pattern.compile("(COM\\d+)[,，](\\d{6,15})[,，](.*)");
        Matcher comMatcher = comPattern.matcher(smsContent);
        if (!comMatcher.find()) {
            // 兼容部分短信格式如“新短信!(号码)号码为[xxx]内容[...]”
            Pattern altPattern = Pattern.compile("新短信!\\((\\d{6,15})\\).*内容\\[(.*?)]");
            Matcher altMatcher = altPattern.matcher(smsContent);
            if (altMatcher.find()) {
                String phoneNumber = altMatcher.group(1);
                String content = altMatcher.group(2);
                String verificationCode = extractVerificationCode(content);
                if (verificationCode != null) {
                    Map<String, String> result = new HashMap<>();
                    result.put("comCode", "未知");
                    result.put("phoneNumber", phoneNumber);
                    result.put("verificationCode", verificationCode);
                    String platform = extractPlatform(content);
                    result.put("platform", platform != null ? platform : "未知平台");
                    return result;
                }
            }
            return null;
        }
        String comCode = comMatcher.group(1);
        String phoneNumber = comMatcher.group(2);
        String content = comMatcher.group(3);
        String verificationCode = extractVerificationCode(content);
        if (verificationCode != null) {
            Map<String, String> result = new HashMap<>();
            result.put("comCode", comCode);
            result.put("phoneNumber", phoneNumber);
            result.put("verificationCode", verificationCode);
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
        // 优化验证码提取，兼容“验证码是xxxx”、“验证码为xxxx”、“code: xxxx”等
        Pattern[] patterns = new Pattern[] {
            Pattern.compile("[驗證碼验证码][是为:：\s]*([0-9]{4,8})"),
            Pattern.compile("code[\s:=]+([0-9]{4,8})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([0-9]{4,8}) is your verification code", Pattern.CASE_INSENSITIVE),
            Pattern.compile("verification code is ([0-9]{4,8})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("\b([0-9]{4,8})\b")
        };
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                return matcher.group(1);
            }
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