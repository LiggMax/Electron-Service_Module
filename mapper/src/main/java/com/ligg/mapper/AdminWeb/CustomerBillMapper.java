package com.ligg.mapper.AdminWeb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.adminweb.CustomerBillEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Ligg
 * @Time 2025/6/4
 *
 * 客户账单
 **/

@Mapper
public interface CustomerBillMapper extends BaseMapper<CustomerBillEntity> {
}
