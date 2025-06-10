package com.ligg.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ligg.common.dto.OrdersDto;
import com.ligg.common.entity.OrderEntity;
import com.ligg.common.entity.ProjectEntity;
import com.ligg.common.utils.SmsParserUtil;
import com.ligg.common.utils.SmsUtil;
import com.ligg.mapper.adminweb.OrderMapper;
import com.ligg.mapper.ProjectMapper;
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
        List<ProjectEntity> projects = projectMapper.selectList(null);
        if (projects.isEmpty()) {
            return null;
        }

        List<Map<String, String>> allResults = new ArrayList<>();

        // 对每个项目的关键字和验证码长度进行短信解析
        for (ProjectEntity project : projects) {
            String keyword = project.getKeyword();
            int codeLength = project.getCodeLength();

            if (keyword == null || keyword.isEmpty() || codeLength <= 0) {
                continue;
            }

            // 使用工具类提取验证码信息
            List<Map<String, String>> resultList = SmsUtil.extractSmsContent(sms, keyword, codeLength);

            if (!resultList.isEmpty()) {
                // 为每个结果添加项目信息
                for (Map<String, String> result : resultList) {
                    result.put("projectId", String.valueOf(project.getProjectId()));
                    result.put("projectName", project.getProjectName());
                    result.put("phoneNumber", result.get("phoneNumber"));
                    result.put("platform", project.getProjectName()); // 保持向后兼容
                    result.put("smsContent", result.get("smsContent")); // 保持向后兼容
                }
                allResults.addAll(resultList);
            }
        }
        log.info("提取的验证码和短信: {}", allResults);
        log.info("提取的短信内容: {}", sms);
        log.info("提取的短信内容: {}", sms);
        return allResults;
    }

    /**
     * 保存验证码更新订单状态
     */
    @Override
    public void saveSmsAndCode(List<Map<String, String>> maps) {
        for (Map<String, String> map : maps) {
            //手机号码
            String phoneNumber = map.get("phoneNumber");
            //短信内容
            String messageContent = map.get("smsContent");

            //查询订单
            List<OrdersDto> orders = orderMapper.selectByPhoneNumber(phoneNumber);

            for (OrdersDto orderDto : orders) {
                //判断短信内容包含订单项目名称
                if (messageContent.contains(orderDto.getProjectName())) {
                    /*
                     * 如果短信中包含这个项目名称，说明这个短信数据该项目，则获取该项目ID
                     */
                    ProjectEntity projectId = projectMapper.selectByProjectName(orderDto.getProjectName());

                    //订单存在或者订单状态为0，则更新
                    if (orderDto.getState() == 0) {
                        userOrderMapper.update(new LambdaUpdateWrapper<OrderEntity>()
                                .eq(OrderEntity::getPhoneNumber, phoneNumber)
                                .eq(OrderEntity::getProjectId, projectId.getProjectId())
                                .set(OrderEntity::getCode, messageContent)
                                .set(OrderEntity::getState, 1));

                        //更新缓存
                        try {
                            orderDto.setOrdersId(orderDto.getOrdersId());
                            orderDto.setCode(messageContent);
                            orderDto.setCreatedAt(LocalDateTime.now());
                            redisTemplate.opsForValue().set("user:orders:" + orderDto.getUserId() + ":" + orderDto.getOrdersId(),
                                    objectMapper.writeValueAsString(orderDto), 20, TimeUnit.MINUTES);
                        } catch (Exception e) {
                            log.error("更新缓存失败: {}", e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
