package com.ligg.service;

import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;

public interface SmsService {

    //获取号码列表
    List<SmsDto> getPhoneNumberList(Long userId);

    //获取验证码列表
    List<CodeVo> getCodeList(Long userId);

    /**
     * 短信验证码推送任务
     */
    void startSmsCodePushTask(Long userId, SseEmitter emitter);

    /**
     * 停止短信验证码推送任务
     */
    void stopSmsCodePushTask(Long userId);
}
