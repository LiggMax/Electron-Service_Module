package com.ligg.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<UserEntity> {

    /**
     * 更新用户余额
     * 用于客户订单退款
     */
    int updateUserMoney(Long userId, Float money);
}
