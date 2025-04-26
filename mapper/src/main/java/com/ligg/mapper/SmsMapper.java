package com.ligg.mapper;

import com.ligg.common.dto.SmsDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmsMapper {


    //获取用户短信列表
    @MapKey("userProjectId")
    List<SmsDto> getSmsList(Long userId);
}
