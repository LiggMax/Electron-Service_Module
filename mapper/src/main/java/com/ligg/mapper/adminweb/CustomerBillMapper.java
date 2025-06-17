package com.ligg.mapper.adminweb;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ligg.common.entity.adminweb.CustomerBillEntity;
import com.ligg.common.query.CustomerBillQuery;
import com.ligg.common.vo.CustomerBillVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Ligg
 * @Time 2025/6/4
 * <p>
 * 客户账单
 **/

@Mapper
public interface CustomerBillMapper extends BaseMapper<CustomerBillEntity> {

    /**
     * 查询客户账单
     */
    Page<CustomerBillVo> selectCustomersBillPage(@Param("page") IPage<CustomerBillQuery> page);
}
