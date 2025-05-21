package com.ligg.mapper.AdminWeb;

import com.ligg.common.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    //获取所有订单信息
    List<OrderVo> getAllOrder();
}
