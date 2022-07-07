package com.itheima.reggie01.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动填充第二步：实现MetaObjectHandler接口
 * 目的：为了每一个实体对象加了自动填充策略的注解的属性填充数据
 */
@Component //自动填充类进行注册
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.debug("************************insertFill************************");
        //设置填充哪个字段值 指定实体对象中属性的名称 和属性对应值
        //注意：属性跟字段有映射关系的 指定属性就等同于指定字段值
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.debug("************************insertFill************************");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long id = Thread.currentThread().getId();
        log.debug("MyMetaObjectHandler:::::::::::线程id:{}",id);
        log.debug("************************updateFill************************");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.debug("************************updateFill************************");
    }
}
