package com.ligg.entrance.exception;

import com.ligg.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//全局异常处理
@Slf4j
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("全局异常捕获：{}",e.getMessage());
        return Result.error(400,StringUtils.hasLength(e.getMessage())?e.getMessage(): "服务异常，请联系管理员");
    }

}