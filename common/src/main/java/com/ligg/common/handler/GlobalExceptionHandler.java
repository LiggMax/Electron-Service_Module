package com.ligg.common.handler;

import com.ligg.common.exception.AuthException;
import com.ligg.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Ligg
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理权限验证异常
     */
    @ExceptionHandler(AuthException.class)
    public Result<String> handleAuthException(AuthException e) {
        log.warn("权限验证失败: {}", e.getErrorMessage());
        return Result.error(e.getErrorCode(), e.getErrorMessage());
    }

    /**
     * 处理其他运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常: {}", e.getMessage(), e);
        return Result.error(500, "系统内部错误");
    }

    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error(500, "系统异常");
    }
} 