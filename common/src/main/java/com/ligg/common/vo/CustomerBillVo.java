package com.ligg.common.vo;

import com.ligg.common.entity.adminweb.CustomerBillEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author Ligg
 * @Time 2025/6/5
 *
 * 客户账单VO
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerBillVo extends CustomerBillEntity {

    /**
     * 客户昵称
     */
    private String nickName;

    /**
     * 客户头像
     */
    private String userAvatar;

    /**
     * 客户账号
     */
    private String account;
}
