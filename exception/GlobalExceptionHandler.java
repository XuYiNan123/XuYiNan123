package com.itheima.reggie01.exception;

import com.itheima.reggie01.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     * 捕获业务异常 返回错误信息给页面
     */
    @ExceptionHandler(BusinessException.class)
    public R<String> businessException(BusinessException businessException){
        String message = businessException.getMessage();//错误信息
        log.debug("业务异常：{}",message);
        return R.error(message);
    }

    /**
     * 未知异常处理
     */
    @ExceptionHandler(Exception.class)
    public R<String> exception(Exception exception){
        String message = exception.getMessage();//错误信息
        log.debug("未知异常：{}",message);
        return R.error("系统繁忙，请稍微再试");
    }
}
