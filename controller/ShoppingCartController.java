package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Setmeal;
import com.itheima.reggie01.service.ShoppingService;

import com.itheima.reggie01.entity.ShoppingCart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingService shoppingService;
    @PostMapping("/add")
    public R<String>  save(@RequestBody ShoppingCart cart){
        shoppingService.add(cart);

        return R.success("加入购物车成功~！");
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        List<ShoppingCart> list = shoppingService.list();
        return R.success(list);
    }
    @DeleteMapping("/clean")
    public R<String> clean(){
        shoppingService.clean();
        return R.success("清空购物车成功！");
    }
}
