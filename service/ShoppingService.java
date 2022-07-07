package com.itheima.reggie01.service;

import com.itheima.reggie01.entity.ShoppingCart;

import java.util.List;

public interface ShoppingService {
    void add(ShoppingCart cart);

    List<ShoppingCart> list();

    void clean();
}
