package com.ligg.mapper;

import com.ligg.common.dto.SmsDto;
import com.ligg.common.vo.CodeVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmsMapper {


    //获取客户号码列表
//    @MapKey("userProjectId")
    List<SmsDto> getSmsList(Long userId);
    //获取用户验证码列表
    List<CodeVo> getCodeList(Long userId);
}
