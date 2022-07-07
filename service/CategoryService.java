package com.itheima.reggie01.service;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Category;

import java.util.List;

/**
 * 分类业务接口
 */
public interface CategoryService {

    R<String> save(Category category);


    R<PageResult> findPage(Long page, Long pageSize);


    R<String> deleteById(Long id);


    R<String> update(Category category);

    List<Category> list(Integer type);
}
