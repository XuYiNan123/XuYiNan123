package com.itheima.reggie01.exception;

/**
 * 自定义业务异常类
 *
 * RuntimeException：程序运行过程中有可能抛出RuntimeException异常的
 * 代码中自己抛出RuntimeException  跟 程序本身抛出RuntimeException 分不清楚到底谁抛出的异常
 *
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message){
        super(message);
    }
}
