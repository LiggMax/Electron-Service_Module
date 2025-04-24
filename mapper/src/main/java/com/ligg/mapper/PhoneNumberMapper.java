package com.ligg.mapper;

import com.ligg.common.dto.PhoneDetailDTO;
import com.ligg.common.entity.Phone;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 批量插入手机号
     * @param phones 手机号列表
     * @return 插入成功的条数
     */
    int batchInsertPhones(List<Phone> phones);

    /**
     * 插入手机号项目
     */
    int insertPhoneProject(@Param("phoneNumber") Long phoneNumber, @Param("projectName") String projectName);

}
