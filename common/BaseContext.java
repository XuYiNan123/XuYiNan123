package com.itheima.reggie01.common;

/**
 * 使用ThreadLocal方法
 * 第一步：导入BaseContext工具类 主要使用set get两个方法
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 */
public class BaseContext {
    //因存入的使用户id所以指定Long类型
    //如果存入的是员工对象 指定Employee对象
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}