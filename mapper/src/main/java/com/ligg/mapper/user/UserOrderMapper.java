package com.ligg.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserOrderMapper extends BaseMapper<OrderEntity> {
}
