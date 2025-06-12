package com.ligg.mapper.adminweb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.adminweb.CustomerBillEntity;
import com.ligg.common.vo.CustomerBillVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Ligg
 * @Time 2025/6/4
 *
 * 客户账单
 **/

@Mapper
public interface CustomerBillMapper extends BaseMapper<CustomerBillEntity> {

    /**
     * 查询客户账单
     */
    List<CustomerBillVo> selectCustomersBill();
}
