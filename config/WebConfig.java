package com.itheima.reggie01.config;

import com.itheima.reggie01.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * 搭建工程环境 第六步：配置静态资源映射
 * springboot默认访问静态资源，你的静态资源需要放到static目录下
 */
@Slf4j
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler("/backend/**")：配置是页面用户输入访问的路径
        //.addResourceLocations("")：当路径匹配后，资源访问具体目录
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        //control+d
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        super.addResourceHandlers(registry);
        log.debug("**************静态资源映射被加载了**************");
    }


    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置自定义消息转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将自定义消息转换器放入集合中 注意位置下标7之前即可
        converters.add(6,messageConverter);
    }
}
