package com.ligg.common.entity.adminweb;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("account_funds")
public class AccountFundsEntity {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long accountId;//平台账号id
    private Float money;//账户余额
}
