package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.*;
import com.itheima.reggie01.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;



    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功!");
    }

    @GetMapping("/page")
    public R<PageResult> findPage(int page, int pageSize, String name){
        PageResult pageResult = setmealService.findPage(page,pageSize,name);
        return R.success(pageResult);
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithFlavor(setmealDto);
        return R.success("更新菜品成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status,Long[] ids){
        setmealService.changeStatus(status,ids);
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Long categoryId, Integer status){
        //1.调用Service处理
        List<Setmeal> list = setmealService.list(categoryId,status);
        //2.响应处理结果
        return R.success(list);
    }
}
