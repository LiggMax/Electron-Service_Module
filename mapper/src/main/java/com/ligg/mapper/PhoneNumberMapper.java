package com.ligg.mapper;

import com.ligg.common.dto.PhoneDetailDTO;
import com.ligg.common.entity.Phone;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhoneNumberMapper {

    /**
     * 条件查询卡号数据
     */
    List<Phone> phoneList(String countryCode, Integer usageStatus, String keyword);

    /**
     * 根据手机号id查询详情
     */
    List<PhoneDetailDTO> queryByIdPhoneDetail(Integer phoneId);
}
