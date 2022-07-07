package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie01.entity.User;
import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.UserMapper;
import com.itheima.reggie01.service.UserService;

import com.itheima.reggie01.utils.ValidateCodeUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void sendMsg(String phone, HttpSession session) {
        String validateCode = ValidateCodeUtils.generateValidateCode(4).toString();


        System.out.println("validateCode = " + validateCode);


        session.setAttribute("validateCode",validateCode);
    }

    @Override
    public User login(Map<String, Object> map, HttpSession session) {
        Object validateCode = session.getAttribute("validateCode");
        if(validateCode==null){
            throw new BusinessException("请重新获取验证码！");
        }


        String code = map.get("code").toString();
        if(code==null || !code.equals(validateCode)){
            throw new BusinessException("请输入正确的验证码！");
        }


        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, map.get("phone"));
        User user = userMapper.selectOne(wrapper);

        if(user==null){
            user = new User();
            user.setPhone(map.get("phone").toString());
            userMapper.insert(user);
        }


        session.setAttribute("user",user);


        return user;
    }
}
