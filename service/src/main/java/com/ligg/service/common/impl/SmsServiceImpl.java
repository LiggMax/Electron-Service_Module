package com.ligg.service.common.impl;

import com.ligg.common.dto.OrdersDto;
import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;
import com.ligg.mapper.AdminWeb.OrderMapper;
import com.ligg.mapper.SmsMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.common.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private UserOrderMapper userOrderMapper;

    @Autowired
    private OrderMapper  orderMapper;

    @Override
    public List<SmsDto> getSmsList(Long userId) {
        return smsMapper.getSmsList(userId);
    }

    /**
     * 获取验证码列表
     */
    @Override
    public List<CodeVo> getCodeList(Long userId) {
        List<CodeVo> codeList = smsMapper.getCodeList(userId);
        //过滤codeList中的空数据
        codeList.removeIf(codeVo -> codeVo.getCode() == null);
        return  codeList;
    }

    @Override
    public List<OrdersDto> getOrdersList(Long userId) {
        return orderMapper.selectByUserId(userId);
    }
}
