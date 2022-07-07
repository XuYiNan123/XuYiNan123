package com.itheima.reggie01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 搭建工程环境 第四步：启动类
 * 搭建工程环境 第五步：将静态前端资源复制到resources目录下
 */
@SpringBootApplication
@Slf4j
@ServletComponentScan //完善登录功能 第二步：扫描过滤器类 注册到spring容器中
public class Reggie01App {

    public static void main(String[] args) {
        SpringApplication.run(Reggie01App.class,args);
    }
}
