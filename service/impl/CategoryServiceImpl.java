package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Category;
import com.itheima.reggie01.entity.Dish;
import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.CategoryMapper;
import com.itheima.reggie01.mapper.DishMapper;
import com.itheima.reggie01.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 分类业务处理实现类
 */
@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    @Autowired
    private DishMapper dishMapper;


    @Override
    public R<String> save(Category category) {
        //  a.根据分类名称查询分类表category 分类数据是否存在
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getName, category.getName());
        Category cg = categoryMapper.selectOne(queryWrapper);
        //  b.如果存在 抛出异常告知用户分类已经存在
        if (cg != null) {
            throw new BusinessException("新增菜品或套餐分类名称必须唯一");
        }
        //  c.如果不存在，则直接可以保存数据到分类表中
        categoryMapper.insert(category);
        return R.success("新增分类成功了");
    }


    @Override
    public R<PageResult> findPage(Long page, Long pageSize) {
        //a.封装分页参数 当前页码 每页显示记录数
        Page pg = new Page(page, pageSize);
        //b.根据表中sort字段值进行降序排序 （降序-从大到小 升序-从小到大）
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        //desc:降序 asc:升序
        lambdaQueryWrapper.orderByDesc(Category::getSort);
        //c.调用selectPage进行分页
        categoryMapper.selectPage(pg, lambdaQueryWrapper);
        //d.获取分页数据 总记录数 当前页面数据
        PageResult pageResult = new PageResult();
        pageResult.setRecords(pg.getRecords());//当前页面显示的数据
        pageResult.setTotal(pg.getTotal());//总记录数
        return R.success(pageResult);
    }


    @Override
    public R<String> deleteById(Long id) {
        //1.根据分类id查询菜品表数据是否存在，如果存在则不能删除给出相应提示
        //方式一：调用菜品持久层接口进行查询(业务相对比较简单)
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        Integer count = dishMapper.selectCount(queryWrapper);
        //方式二：调用菜品业务层接口进行查询(业务比较复杂)
        //dishService.selectCount(xxx);
        //2.如果数据不存在，则直接可以删除分类数据
        if (count > 0) {
            throw new BusinessException("分类已经关联数据不能删除");
        }
        //3.返回分类删除成功结果
        categoryMapper.deleteById(id);
        return R.success("分类删除成功了");
    }


    @Override
    public R<String> update(Category category) {
        categoryMapper.updateById(category);
        return R.success("分类修改成功了");
    }

    @Override
    public List<Category> list(Integer type) {
        //1.调用Mapper根据type=1查询category表 得到菜品分类列表数据
        //1.1:创建LambdaQueryWrapper对象
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        //1.2：设置查询条件
        wrapper.eq(type!=null,Category::getType,type);
        //1.3：执行查询 传入wrapper对象
        List<Category> list = categoryMapper.selectList(wrapper);

        //2.返回菜品分类列表数据集合对象
        return list;
    }
}
