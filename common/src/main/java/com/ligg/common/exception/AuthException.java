package com.ligg.common.exception;

/**
 * 权限验证异常
 * 当权限验证失败时抛出此异常
 *
 * @author Ligg
 */
public class AuthException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public AuthException(int errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public AuthException(String errorMessage) {
        this(401, errorMessage);
    }

    public AuthException() {
        this(401, "无权限访问");
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
} 