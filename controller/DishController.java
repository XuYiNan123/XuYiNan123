package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Category;
import com.itheima.reggie01.entity.Dish;
import com.itheima.reggie01.entity.DishDto;
import com.itheima.reggie01.entity.Employee;
import com.itheima.reggie01.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功!");
    }
    @GetMapping("/page")
    public R<PageResult> findPage(int page, int pageSize, String name){
        PageResult pageResult = dishService.findPage(page,pageSize,name);
        return R.success(pageResult);
    }

    @GetMapping("/{id}")
    public R<DishDto> findById(@PathVariable Long id){
        DishDto dishDto = dishService.findById(id);
        return R.success(dishDto);
    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新菜品成功");
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Long categoryId){
        //1.调用service处理
        List<DishDto> list = dishService.list(categoryId);

        //2.响应菜品列表数据
        return R.success(list);
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable int status,Long[] ids){


        dishService.updateStatus(status,ids);
        return R.success("修改成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        dishService.deleteWithDish(ids);
        return R.success("删除菜品成功");
    }

}
