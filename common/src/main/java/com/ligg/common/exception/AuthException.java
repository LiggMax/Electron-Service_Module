package com.ligg.common.exception;

import lombok.Getter;

/**
 * 权限验证异常
 * 当权限验证失败时抛出此异常
 *
 * @author Ligg
 */
@Getter
public class AuthException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public AuthException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}