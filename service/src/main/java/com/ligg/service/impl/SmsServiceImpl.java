package com.ligg.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ligg.common.dto.SmsDto;
import com.ligg.common.entity.UserOrderEntity;
import com.ligg.common.vo.CodeVo;
import com.ligg.mapper.SmsMapper;
import com.ligg.mapper.user.UserOrderMapper;
import com.ligg.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    private SmsMapper smsMapper;

    @Autowired
    private UserOrderMapper userOrderMapper;

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
        for (CodeVo codeVo : codeList) {
            userOrderMapper.update(new LambdaUpdateWrapper<UserOrderEntity>()
                    .eq(UserOrderEntity::getUserId,  userId)
                    .and(wrapper -> wrapper.eq(UserOrderEntity::getPhoneNumber,codeVo.getPhoneNumber()))
                    .set(UserOrderEntity::getState, 1));
        }
        //过滤codeList中的空数据
        codeList.removeIf(codeVo -> codeVo.getCode() == null);
        return  codeList;
    }
}
