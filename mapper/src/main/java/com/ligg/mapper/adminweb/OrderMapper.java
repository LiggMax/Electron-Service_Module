package com.ligg.mapper.adminweb;

import com.ligg.common.dto.OrdersDto;
import com.ligg.common.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    //获取所有订单信息
    List<OrderVo> getAllOrder();

    /**
     * 根据号码查询订单信息
     */
    List<OrdersDto> selectByPhoneNumber(String phoneNumber);

    /**
     * 根据订用户id查询订单信息
     */
    List<OrdersDto> selectByUserId(Long userId);
}
