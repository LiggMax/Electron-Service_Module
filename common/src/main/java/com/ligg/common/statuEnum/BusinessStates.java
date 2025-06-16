package com.ligg.common.statuEnum;

import lombok.Getter;

/**
 * 业务状态码枚举
 */
@Getter
public enum BusinessStates {
    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 请求参数错误
     */
    BAD_REQUEST(400, "请求参数错误"),

    /**
     * 未授权访问
     */
    UNAUTHORIZED(401, "未授权访问"),

    /**
     * 禁止访问
     */
    FORBIDDEN(403, "禁止访问"),

    /**
     * 资源不存在
     */
    NOT_FOUND(404, "资源不存在"),

    /**
     * 方法不允许
     */
    METHOD_NOT_ALLOWED(405, "方法不允许"),

    /**
     * 内部服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "内部服务器错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 数据验证失败
     */
    VALIDATION_FAILED(1001, "数据验证失败"),

    /**
     * 登录已过期
     */
    LOGIN_EXPIRED(1002, "登录已过期"),
    FILE_UPLOAD_FAILED(1003, "文件上传失败"),
    DATA_NOT_FOUND(1004, "数据不存在");

    private final int code;
    private final String message;

    BusinessStates(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
