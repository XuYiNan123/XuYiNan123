package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.User;
import com.itheima.reggie01.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取验证码
     * @param phone
     * @param session
     * @return
     */
    @GetMapping("/sendMsg")
    public R<String> sendMsg(String phone, HttpSession session){
        //1.调用Service处理
        userService.sendMsg(phone,session);

        //2.响应处理结果
        return R.success("验证码发送成功！");
    }

    /**
     * 用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String,Object> map, HttpSession session){
        //1.调用Service处理
        User user = userService.login(map,session);

        //2.响应处理结果
        return R.success(user);
    }
}
