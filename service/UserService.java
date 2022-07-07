package com.itheima.reggie01.service;

import com.itheima.reggie01.entity.User;

import javax.servlet.http.HttpSession;
import java.util.Map;

public interface UserService {
    void sendMsg(String phone, HttpSession session);

    User login(Map<String, Object> map, HttpSession session);
}
