package com.itheima.reggie01.service;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.DishDto;
import com.itheima.reggie01.entity.Setmeal;
import com.itheima.reggie01.entity.SetmealDish;
import com.itheima.reggie01.entity.SetmealDto;

import java.util.List;

public interface SetmealService {


    void saveWithDish(SetmealDto setmealDto);

    PageResult findPage(int page, int pageSize, String name);
    void updateWithFlavor(SetmealDto setmealDto);

    void deleteWithDish(List<Long> ids);


    void changeStatus(int status, Long[] ids);

    List<Setmeal> list(Long categoryId, Integer status);
}
