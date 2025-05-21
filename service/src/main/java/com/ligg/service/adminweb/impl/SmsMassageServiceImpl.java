package com.ligg.service.adminweb.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ligg.common.entity.UserOrderEntity;
import com.ligg.common.utils.SmsParserUtil;
import com.ligg.mapper.user.UserMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.adminweb.SmsMassageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SmsMassageServiceImpl implements SmsMassageService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserOrderMapper userOrderMapper;

    /**
     * 提取验证码和短信
     */
    @Override
    public List<Map<String, String>> extractCodeAndSms(String sms) {
        if (sms == null || sms.isEmpty()) {
            System.out.println("短信内容为空");
            return null;
        }

        // 使用工具类提取验证码信息
        List<Map<String, String>> resultList = SmsParserUtil.extractVerificationCodes(sms);

        if (resultList.isEmpty()) {
            log.info("未匹配到任何验证码信息");
        } else {
            System.out.println("共提取到 " + resultList.size() + " 条验证码信息");
            for (Map<String, String> result : resultList) {
                System.out.println("提取成功 - 平台: " + result.get("platform") +
                        ", 号码: " + result.get("phoneNumber") +
                        ", 验证码: " + result.get("verificationCode"));
            }
        }
        return resultList;
    }

    /**
     * 保存验证码更新订单状态
     */
    @Override
    public void saveSmsAndCode(List<Map<String, String>> maps) {
        for (Map<String, String> map : maps) {
            String phoneNumber = map.get("phoneNumber");
            String verificationCode = map.get("verificationCode");

            //查询订单
            List<UserOrderEntity> orders = userOrderMapper.selectList(new LambdaQueryWrapper<UserOrderEntity>()
                    .eq(UserOrderEntity::getPhoneNumber, phoneNumber));
            //订单不存在或者订单状态为0，则更新
            if (orders.isEmpty() || orders.get(0).getState() == 0){
                userOrderMapper.update(new LambdaUpdateWrapper<UserOrderEntity>()
                        .eq(UserOrderEntity::getPhoneNumber, phoneNumber)
                        .set(UserOrderEntity::getCode, verificationCode)
                        .set(UserOrderEntity::getState, 1));
            }

        }
    }
}
