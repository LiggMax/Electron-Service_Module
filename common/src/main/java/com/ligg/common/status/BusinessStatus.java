package com.ligg.common.status;

import lombok.Data;

/**
 * 业务状态码枚举
 */

public enum BusinessStatus {
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    REQUEST_TIMEOUT(408, "请求超时"),
    DUPLICATE_ENTRY(409, "数据冲突，已存在相同记录"),
    VALIDATION_FAILED(422, "数据校验失败");

    private final int code;
    private final String message;

    BusinessStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
