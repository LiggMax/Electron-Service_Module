package com.ligg.mapper.AdminWeb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Ligg
 * @Time 2025/6/5
 * <p>
 * 订单账单
 **/
@Mapper
public interface OrderBillMapper extends BaseMapper<OrderBillEntity> {
}
