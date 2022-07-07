package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.*;
import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.CategoryMapper;
import com.itheima.reggie01.mapper.SetmealDishMapper;
import com.itheima.reggie01.mapper.SetmealMapper;
import com.itheima.reggie01.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if(!CollectionUtils.isEmpty(setmealDishes)){
            setmealDishes = setmealDishes.stream().map(
                    item->{
                        item.setSetmealId(setmealDto.getId());
                        return item;
                    }
            ).collect(Collectors.toList());
            for (SetmealDish setmealDish:setmealDishes){
                setmealDishMapper.insert(setmealDish);
            }
        }
    }

    @Override
    public PageResult findPage(int page, int pageSize, String name) {

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name!=null&&name.length()>0,Setmeal::getName,name);
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        setmealMapper.selectPage(setmealPage,queryWrapper);

        List<Setmeal> setmealList = setmealPage.getRecords();
        //b:创建List<SetmealDto>集合
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        if(setmealList!=null && setmealList.size()>0){
            for (Setmeal setmeal : setmealList) {
                SetmealDto setmealDto = new SetmealDto();

                //将每一个setmeal对象的属性数据复制给setmealDto对象
                BeanUtils.copyProperties(setmeal,setmealDto);

                //查询当前套餐所属的套餐分类名称
                Category category = categoryMapper.selectById(setmeal.getCategoryId());
                String categoryName = category==null?"未知":category.getName();       //判断分类是否存在：原因：删除分类时没有判断分类下是否关联套餐 导致分类下有套餐也可以删除
                setmealDto.setCategoryName(categoryName);


                //将setmealDto对象存入setmealDtoList集合中
                setmealDtoList.add(setmealDto);
            }
        }

        PageResult<SetmealDto> pageResult = new PageResult<>();
        pageResult.setTotal(setmealPage.getTotal());
        pageResult.setRecords(setmealDtoList);

        return pageResult;


    }

    @Override
    public void updateWithFlavor(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();


        queryWrapper.eq(Setmeal::getName, setmealDto.getName());
        queryWrapper.ne(Setmeal::getId, setmealDto.getId());
        Integer count = setmealMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException("菜品【"+setmealDto.getName()+"】已经存在！");
        }

        setmealMapper.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getDishId, setmealDto.getId());
        setmealDishMapper.delete(setmealDishLambdaQueryWrapper);
        List<SetmealDish> flavors = setmealDto.getSetmealDishes();
        if (flavors != null && flavors.size() > 0) {
            for (SetmealDish flavor : flavors) {
                flavor.setDishId(setmealDto.getId());
                setmealDishMapper.insert(flavor);
            }
        }
    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        Integer count = setmealMapper.selectCount(queryWrapper);
        //方式二：调用菜品业务层接口进行查询(业务比较复杂)
        //dishService.selectCount(xxx);
        //2.如果数据不存在，则直接可以删除分类数据
        if (count > 0) {
            throw new BusinessException("不能删除，正在售卖");
        }
        setmealMapper.deleteBatchIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishMapper.delete(lambdaQueryWrapper);

    }

    @Override
    public void changeStatus(int status, Long[] ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids != null, Setmeal::getId, ids);
        List<Setmeal> list = setmealMapper.selectList(queryWrapper);
        for (Setmeal setmeal : list) {
            if (setmeal != null) {
                setmeal.setStatus(status);
                setmealMapper.updateById(setmeal);
            }
        }
    }

    @Override
    public List<Setmeal> list(Long categoryId, Integer status) {
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(categoryId!=null,Setmeal::getCategoryId,categoryId);
        wrapper.eq(status!=null,Setmeal::getStatus,status);

        //2.执行查询方法 传入查询条件
        List<Setmeal> setmealList = setmealMapper.selectList(wrapper);

        //3.返回套餐列表数据集合
        return setmealList;
    }



}
