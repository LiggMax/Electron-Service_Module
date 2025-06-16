package com.ligg.common.statuEnum;

import lombok.Getter;

/**
 * @Author Ligg
 * @Time 2025/6/16
 **/
@Getter
public enum OrderState {
    UNUSED(0, "订单还未使用，不可结算"),
    IN_USE(1, "订单正在使用，不可结算"),
    AVAILABLE_FOR_SETTLE(2, "待结算"),
    SETTLED(3, "订单已结算");

    private final int code;
    private final String message;

    OrderState(int code, String message) {
        this.code = code;
        this.message = message;
    }

    //  根据code获取枚举值
    public static OrderState fromCode(int code) {
        for (OrderState state : values()) {
            if (state.code == code) {
                return state;
            }
        }
        return null;
    }

}
