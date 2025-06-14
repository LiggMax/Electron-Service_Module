package com.ligg.service.common.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.dto.OrdersDto;
import com.ligg.common.entity.adminweb.ProjectKeyWordEntity;
import com.ligg.common.utils.SmsUtil;
import com.ligg.mapper.adminweb.OrderMapper;
import com.ligg.mapper.adminweb.ProjectKeyWordMapper;
import com.ligg.service.common.SmsMassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsMassageServiceImpl implements SmsMassageService {


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProjectKeyWordMapper projectKeyWordMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    /**
     * 提取验证码和短信
     */
    @Override
    public List<Map<String, String>> extractCodeAndSms(String sms) {
        if (sms == null || sms.isEmpty()) {
            return null;
        }

        // 获取所有项目配置
        List<ProjectKeyWordEntity> keyWords = projectKeyWordMapper.selectList(null);
        if (keyWords.isEmpty()) {
            return null;
        }
        log.info("接收到的初始短信内容：{}", sms);
        List<Map<String, String>> allResults = new ArrayList<>();

        // 对每个项目的关键字和验证码长度进行短信解析
        for (ProjectKeyWordEntity projectKeyWord : keyWords) {
            String keyword = projectKeyWord.getKeyword();
            int codeLength = projectKeyWord.getCodeLength();

            if (keyword == null || keyword.isEmpty() || codeLength <= 0) {
                continue;
            }

            // 使用工具类提取验证码信息
            List<Map<String, String>> resultList = SmsUtil.extractSmsContent(sms, keyword, codeLength);

            if (!resultList.isEmpty()) {
                // 为每个结果添加项目信息
                for (Map<String, String> result : resultList) {
                    result.put("phoneNumber", result.get("phoneNumber"));
                    result.put("verificationCode", result.get("verificationCode"));
                    result.put("smsContent", result.get("smsContent"));
                }
                allResults.addAll(resultList);
            }
        }
        log.info("解析后的短信内容：{}", allResults);
        return allResults;
    }


    /**
     * 保存验证码并更新订单状态到缓存
     */
    @Override
    public void saveSmsAndCode(List<Map<String, String>> allMessageResults) {
        if (allMessageResults == null || allMessageResults.isEmpty()) {
            return;
        }

        for (Map<String, String> map : allMessageResults) {
            String phoneNumber = map.get("phoneNumber");
            String messageContent = map.get("smsContent");
            String code = map.get("verificationCode");

            if (phoneNumber == null || messageContent == null || code == null) {
                continue;
            }

            List<OrdersDto> ordersList = orderMapper.selectByPhoneNumber(phoneNumber);
            if (ordersList == null || ordersList.isEmpty()) {
                continue;
            }

            // 获取所有项目的关键词
            List<ProjectKeyWordEntity> allKeywords = projectKeyWordMapper.selectList(null);
            if (allKeywords == null || allKeywords.isEmpty()) {
                return;
            }

            // 构建项目ID -> 关键词映射
            Map<Integer, List<String>> keywordMap = buildKeywordMap(allKeywords);

            // 处理每个订单
            for (OrdersDto orderDto : ordersList) {
                List<String> keywords = keywordMap.get(orderDto.getProjectId());
                if (keywords == null || keywords.isEmpty()) {
                    continue;
                }

                for (String keyword : keywords) {
                    if (messageContent.contains(keyword)) {
                        updateOrderAndCache(orderDto, code, phoneNumber);
                        break; // 匹配成功后无需继续匹配其他关键词
                    }
                }
            }
        }
    }

    /**
     * 构建 projectId 到关键词列表的映射
     */
    private Map<Integer, List<String>> buildKeywordMap(List<ProjectKeyWordEntity> allKeywords) {
        Map<Integer, List<String>> keywordMap = new HashMap<>();
        for (ProjectKeyWordEntity entity : allKeywords) {
            keywordMap.computeIfAbsent(entity.getProjectId(), k -> new ArrayList<>()).add(entity.getKeyword());
        }
        return keywordMap;
    }

    /**
     * 更新订单信息并写入缓存
     */
    private void updateOrderAndCache(OrdersDto orderDto, String code, String phoneNumber) {
        try {
            orderDto.setCode(code);
            orderDto.setCreatedAt(LocalDateTime.now());

            // 使用独立 Key 存储每个验证码
            String key = "phone:" + phoneNumber + ":code:" + code;
            String hashKey = "phone:" + phoneNumber + ":codes";

            String json = objectMapper.writeValueAsString(orderDto);

            // 设置独立验证码缓存（20分钟过期）
            redisTemplate.opsForValue().set(key, json, 20, TimeUnit.MINUTES);

            // 可选：同时写入 Hash 结构中便于查询所有验证码
            redisTemplate.opsForHash().put(hashKey, code, json);
            redisTemplate.expire(hashKey, 20, TimeUnit.MINUTES); // 可选：为 Hash 设置统一过期时间
        } catch (Exception e) {
            log.error("添加短信缓存失败", e);
        }
    }

}
