package com.ligg.mapper.adminweb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.adminweb.OrderBillEntity;
import com.ligg.common.query.OrderBillQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Ligg
 * @Time 2025/6/5
 * <p>
 * 订单账单
 **/
@Mapper
public interface OrderBillMapper extends BaseMapper<OrderBillEntity> {

    /**
     * 查询订单账单
     */
    Page<OrderBillEntity> selectOrderBillPage(@Param("page") Page<OrderBillEntity> page,
                                              @Param("query") OrderBillQuery query);
}
