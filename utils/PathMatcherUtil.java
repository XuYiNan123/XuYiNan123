package com.itheima.reggie01.utils;

import org.springframework.util.AntPathMatcher;

/**
 * 路径匹配器工具方法
 */
public class PathMatcherUtil {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    public static boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
