package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.dto.OrdersDto;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.entity.adminweb.ProjectKeyWordEntity;
import com.ligg.common.utils.SmsUtil;
import com.ligg.mapper.adminweb.OrderMapper;
import com.ligg.mapper.ProjectMapper;
import com.ligg.mapper.adminweb.ProjectKeyWordMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.common.SmsMassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class SmsMassageServiceImpl implements SmsMassageService {

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProjectMapper projectMapper;

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
     * 保存验证码更新订单状态
     */
    @Override
    public void saveSmsAndCode(List<Map<String, String>> allMessageResults) {
        for (Map<String, String> map : allMessageResults) {
            //手机号码
            String phoneNumber = map.get("phoneNumber");
            //短信内容
            String messageContent = map.get("smsContent");

            String code = map.get("verificationCode");
            //查询订单
            List<OrdersDto> ordersProjectName = orderMapper.selectByPhoneNumber(phoneNumber);
            for (OrdersDto orderDto : ordersProjectName) {
                List<ProjectKeyWordEntity> projectKeyWord = projectKeyWordMapper.selectList(new LambdaQueryWrapper<ProjectKeyWordEntity>()
                        .eq(ProjectKeyWordEntity::getProjectId, orderDto.getProjectId()));
                /*
                 * 如果短信中包含这个项目名称，说明这个短信数据该项目，则获取该项目ID
                 */
                for (ProjectKeyWordEntity project : projectKeyWord) {
                    if (messageContent.contains(project.getKeyword())) {
                        try {
                            orderDto.setOrdersId(orderDto.getOrdersId());
                            orderDto.setCode(code);
                            orderDto.setCreatedAt(LocalDateTime.now());
                            redisTemplate.opsForHash().put("phone:" + phoneNumber + ":orderId" + orderDto.getOrdersId(),
                                    orderDto.getCode(), objectMapper.writeValueAsString(orderDto));
                            //设置过期时间
                            redisTemplate.expire("phone:" + phoneNumber + ":orderId" + orderDto.getOrdersId(),
                                    20, TimeUnit.MINUTES
                            );
                        } catch (Exception e) {
                            log.error("添加短信缓存失败: {}", e.getMessage());
                        }
                    }
                }
//                if (messageContent.contains(projectKeyWord.getKeyword())) {
//                    try {
//                        orderDto.setOrdersId(orderDto.getOrdersId());
//                        orderDto.setCode(code);
//                        orderDto.setCreatedAt(LocalDateTime.now());
//                        redisTemplate.opsForHash().put("phone:" + phoneNumber + ":orderId" + orderDto.getOrdersId(),
//                                orderDto.getCode(), objectMapper.writeValueAsString(orderDto));
//                        //设置过期时间
//                        redisTemplate.expire("phone:" + phoneNumber + ":orderId" + orderDto.getOrdersId(),
//                                5, TimeUnit.MINUTES
//                        );
//                    } catch (Exception e) {
//                        log.error("添加短信缓存失败: {}", e.getMessage());
//                    }
//                }
            }
        }
    }
}
