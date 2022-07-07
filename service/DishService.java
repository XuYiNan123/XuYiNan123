package com.itheima.reggie01.service;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Dish;
import com.itheima.reggie01.entity.DishDto;

import java.util.List;

public interface DishService {
    void saveWithFlavor(DishDto dishDto);
    PageResult findPage(int page, int pageSize, String name);

    DishDto findById(Long id);

    void updateWithFlavor(DishDto dishDto);

    List<DishDto> list(Long categoryId);


    void updateStatus(int status, Long[] ids);

    void deleteWithDish(List<Long> ids);
}
