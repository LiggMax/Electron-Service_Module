package com.ligg.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ligg.common.entity.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerMapper extends BaseMapper<UserEntity> {


    /**
     * 充值用户余额
     */
    void addUserMoney(Long userId, Float balance);

    /**
     * 扣款用户余额
     */
    void subtractUserMoney(Long userId, Float balance);
}
