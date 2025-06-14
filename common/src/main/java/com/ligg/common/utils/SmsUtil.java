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
     * @param sms 短信内容
     * @param keyword 关键字，用于匹配短信内容中的字段
     * @param codeLength 验证码位数，关键字后面的位数
     * @return 解析结果列表，包含号码和验证码信息
     */
    public static List<Map<String, String>> extractSmsContent(String sms, String keyword, int codeLength) {
        if (sms == null || sms.isEmpty() || keyword == null || keyword.isEmpty() || codeLength <= 0) {
            return new ArrayList<>();
        }
        List<Map<String, String>> resultList = new ArrayList<>();


        // 修改正则表达式，使用COM开头到下一个COM之间的内容作为一条短信
        Pattern comPattern = Pattern.compile("COM(\\d+),(\\d+),([^C]*?)(?=COM|$)");
        Matcher comMatcher = comPattern.matcher(sms);

        int matchCount = 0;
        while (comMatcher.find()) {
            matchCount++;
            String phoneNumber = comMatcher.group(2);       // 号码
            String smsContent = comMatcher.group(3);        // 短信内容

            // 在短信内容中查找关键字并提取验证码
            String verificationCode = extractVerificationCode(smsContent, keyword, codeLength);

            if (verificationCode != null && !verificationCode.isEmpty()) {
                Map<String, String> smsInfo = new HashMap<>();
                smsInfo.put("phoneNumber", phoneNumber);
                smsInfo.put("verificationCode", verificationCode);
                smsInfo.put("smsContent", smsContent);
                resultList.add(smsInfo);
            }
        }

        return resultList;
    }

    /**
     * 从短信内容中根据关键字提取验证码
     *
     * @param content    短信内容
     * @param keyword    关键字
     * @param codeLength 验证码位数
     * @return 验证码
     */
    private static String extractVerificationCode(String content, String keyword, int codeLength) {
        if (content == null || content.isEmpty()) {
            return null;
        }

        // 查找关键字在内容中的位置
        int keywordIndex = content.indexOf(keyword);

        if (keywordIndex == -1) {
            return null;
        }

        // 从关键字后开始查找数字
        String afterKeyword = content.substring(keywordIndex + keyword.length());

        // 使用正则表达式提取所有数字字符
        Pattern codePattern = Pattern.compile("\\d");
        Matcher codeMatcher = codePattern.matcher(afterKeyword);

        StringBuilder allDigits = new StringBuilder();
        while (codeMatcher.find()) {
            allDigits.append(codeMatcher.group());
        }

        // 如果找到的数字字符数量大于等于指定位数，则取前面指定位数的数字
        if (allDigits.length() >= codeLength) {
            return allDigits.substring(0, codeLength);
        }

        return null;
    }
}
