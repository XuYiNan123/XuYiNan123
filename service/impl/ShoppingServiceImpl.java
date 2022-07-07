package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie01.common.BaseContext;
import com.itheima.reggie01.entity.ShoppingCart;

import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.ShoppingMapper;
import com.itheima.reggie01.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ShoppingServiceImpl implements ShoppingService {
    @Autowired
    private ShoppingMapper shoppingMapper;

    @Override
    public void add(ShoppingCart cart) {
        if(cart==null){
            throw new BusinessException("请选择商品后再加入购物车");
        }
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long userId = BaseContext.getCurrentId();
        wrapper.eq(ShoppingCart::getUserId,userId);
        Long dishId = cart.getDishId();
        if(dishId!=null){   //说明当前加入购物车的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);
            wrapper.eq(cart.getDishFlavor()!=null,ShoppingCart::getDishFlavor,cart.getDishFlavor());
        }else{
            wrapper.eq(ShoppingCart::getSetmealId,cart.getSetmealId());
        }
        ShoppingCart shoppingCart = shoppingMapper.selectOne(wrapper);
        if(shoppingCart!=null){
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingMapper.updateById(shoppingCart);
        }else{
            shoppingCart = cart;
            cart.setNumber(1);
            cart.setUserId(userId);
            shoppingMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {

        Long userId = BaseContext.getCurrentId();


        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);


        List<ShoppingCart> shoppingCartList = shoppingMapper.selectList(wrapper);


        return shoppingCartList;
    }
    @Override
    public void clean() {
        //1.获取当前登录用户id  从ThreadLocal中保存的当前登录用户id获取即可
        Long userId = BaseContext.getCurrentId();

        //2.设置删除条件
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);

        //3.执行删除 删除当前登录用户购物车中的商品列表数据
        shoppingMapper.delete(wrapper);
    }
}
