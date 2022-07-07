package com.itheima.reggie01.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Category;
import com.itheima.reggie01.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐或菜品分类控制层
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @PostMapping
    public R<String> save(@RequestBody Category category) {
        return categoryService.save(category);
    }


    @GetMapping("/page")
    public R<PageResult> findPage(@RequestParam(value = "page",defaultValue = "1")Long page,@RequestParam(value = "pageSize",defaultValue = "1")Long pageSize){
        return categoryService.findPage(page,pageSize);
    }



    @DeleteMapping
    //public R<String> deleteById(Long id){
    public R<String> deleteById(@RequestParam("id")Long id){
        return categoryService.deleteById(id);
    }



    @PutMapping
    public R<String> update(@RequestBody Category category){
        return categoryService.update(category);
    }

    @GetMapping("/list")
    public R<List<Category>> list(Integer type){
        //1.调用service处理 得到菜品分类列表数据
        List<Category> list = categoryService.list(type);
        //2.响应菜品分类列表数据给浏览器
        return R.success(list);
    }
}
