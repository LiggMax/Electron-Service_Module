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

        System.out.println("=== 开始解析短信 ===");
        System.out.println("关键字: " + keyword);
        System.out.println("验证码位数: " + codeLength);
        System.out.println("短信长度: " + sms.length());

        // 修改正则表达式，使用COM开头到下一个COM之间的内容作为一条短信
        Pattern comPattern = Pattern.compile("COM(\\d+),(\\d+),([^C]*?)(?=COM|$)");
        Matcher comMatcher = comPattern.matcher(sms);

        int matchCount = 0;
        while (comMatcher.find()) {
            matchCount++;
            String phoneNumber = comMatcher.group(2);       // 号码
            String smsContent = comMatcher.group(3);        // 短信内容

            System.out.println("--- 第" + matchCount + "条短信 ---");
            System.out.println("号码: " + phoneNumber);
            System.out.println("短信内容: " + smsContent);

            // 在短信内容中查找关键字并提取验证码
            String verificationCode = extractVerificationCode(smsContent, keyword, codeLength);

            if (verificationCode != null && !verificationCode.isEmpty()) {
                Map<String, String> smsInfo = new HashMap<>();
                smsInfo.put("phoneNumber", phoneNumber);
                smsInfo.put("verificationCode", verificationCode);

                resultList.add(smsInfo);
                System.out.println("成功添加到结果列表");
            }
        }

        System.out.println("总共匹配到 " + matchCount + " 条短信");
        System.out.println("最终结果数量: " + resultList.size());
        
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
            System.out.println("短信内容为空");
            return null;
        }

        // 查找关键字在内容中的位置
        int keywordIndex = content.indexOf(keyword);
        System.out.println("关键字'" + keyword + "'在内容中的位置: " + keywordIndex);

        if (keywordIndex == -1) {
            System.out.println("未找到关键字");
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

        System.out.println("提取到的所有数字: '" + allDigits.toString() + "'");
        System.out.println("数字总长度: " + allDigits.length() + ", 需要长度: " + codeLength);

        // 如果找到的数字字符数量大于等于指定位数，则取前面指定位数的数字
        if (allDigits.length() >= codeLength) {
            String result = allDigits.substring(0, codeLength);
            System.out.println("提取的验证码: '" + result + "'");
            return result;
        }

        System.out.println("数字长度不足，返回null");
        return null;
    }
}
