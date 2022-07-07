package com.itheima.reggie01.filter;

import com.itheima.reggie01.common.BaseContext;
import com.itheima.reggie01.entity.Employee;
import com.itheima.reggie01.entity.User;
import com.itheima.reggie01.utils.PathMatcherUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 完善登录功能
 */
@WebFilter
@Slf4j
public class LoginCheckFilter implements Filter {


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long id = Thread.currentThread().getId();
        log.debug("LoginCheckFilter:::::::::::线程id:{}",id);
        //1、获取本次请求的URI
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestURI = httpServletRequest.getRequestURI();
        //2、判断本次请求是否需要处理
        //资料中使用工具类实现放行PathMatcherUtil 主要考虑哪些资源需要放行
        String[] urls = new String[]{
            "/**/login.html","/**/api/**","/**/images/**","/**/js/**","/**/plugins/**"
            ,"/**/styles/**","/**/favicon.ico","/**/fonts/**","/employee/login","/employee/logout",
             "/user/sendMsg", "/user/login"
        };
        if (PathMatcherUtil.check(urls,requestURI)) {
            //3、如果不需要处理，则直接放行
            chain.doFilter(request, response);
            return;//这里一定要return
        }
        //4、判断登录状态，如果已登录，则直接放行
        //从session中获取用户是否登录
        HttpSession session = httpServletRequest.getSession();
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee != null) {
            //使用ThreadLocal:第二步在LoginCheckFilter过滤器设置用户信息
            BaseContext.setCurrentId(employee.getId());
            chain.doFilter(request, response);
            return;//这里一定要return
        }
        User user = (User) session.getAttribute("user");
        if (user != null) {
            //使用ThreadLocal:第二步在LoginCheckFilter过滤器设置用户信息
            BaseContext.setCurrentId(user.getId());
            chain.doFilter(request, response);
            return;//这里一定要return
        }
        //5、如果未登录则返回未登录结果
        //跳转登录页面 response中api方法
        if(requestURI.contains("/backend")){
            httpServletResponse.sendRedirect("/backend/page/login/login.html");
        }else{
            httpServletResponse.sendRedirect("/front/page/login.html");
        }
    }
}
