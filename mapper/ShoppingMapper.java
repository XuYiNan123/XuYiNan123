package com.itheima.reggie01.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.itheima.reggie01.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingMapper extends BaseMapper<ShoppingCart> {
}
