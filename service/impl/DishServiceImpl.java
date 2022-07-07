package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie01.common.PageResult;

import com.itheima.reggie01.common.R;

import com.itheima.reggie01.entity.*;
import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.CategoryMapper;
import com.itheima.reggie01.mapper.DishFlavorMapper;
import com.itheima.reggie01.mapper.DishMapper;
import com.itheima.reggie01.service.DishService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.CollectionUtils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;


    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getName,dishDto.getName());
        Dish dish = dishMapper.selectOne(queryWrapper);

        if (dish!=null) {
            throw new BusinessException("菜品["+dishDto.getName()+"]已经存在");
        }
        dishMapper.insert(dishDto);

        Long disId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        if(flavors!= null && flavors.size()>0){
            for (DishFlavor flavor:flavors){
                flavor.setDishId(dishDto.getId());
                dishFlavorMapper.insert(flavor);
            }
        }
    }

    @Override
    public PageResult findPage(int page, int pageSize, String name) {

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name!=null&&name.length()>0,Dish::getName,name);
        Page<Dish> dishPage = new Page<>(page,pageSize);
        dishMapper.selectPage(dishPage,queryWrapper);
            List<Dish> dishList = dishPage.getRecords();
            List<DishDto> dishDtoList = new ArrayList<>();
            for (Dish dish : dishList) {
                //a:创建DishDto对象
                DishDto dishDto = new DishDto();
                //b:将dish对象中的属性数据赋值到dishDto对象属性上
                BeanUtils.copyProperties(dish,dishDto);
                //c:为dishDto对象设置categoryName属性
                //c1:根据当前菜品所属的分类id 去category表中查询分类名称
                Category category = categoryMapper.selectById(dish.getCategoryId());
                //c2：将分类名称设置到dishDto对象的categoryName属性上
                dishDto.setCategoryName(category.getName());

                //d：将dishDto对象存入到dishDtoList集合中
                dishDtoList.add(dishDto);

            }
        PageResult<DishDto> pageResult = new PageResult<>();
        pageResult.setTotal(dishPage.getTotal());
        pageResult.setRecords(dishDtoList);

        return pageResult;


    }

    @Override
    public DishDto findById(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.selectById(id);
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavorList = dishFlavorMapper.selectList(lambdaQueryWrapper);
        dishDto.setFlavors(flavorList);
        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getName, dishDto.getName());
        queryWrapper.ne(Dish::getId, dishDto.getId());
        Dish dish = dishMapper.selectOne(queryWrapper);

        if (dish != null) {
            throw new BusinessException("菜品[" + dishDto.getName() + "]已经存在");
        }

        dishMapper.updateById(dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorMapper.delete(dishFlavorLambdaQueryWrapper);
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishDto.getId());
                dishFlavorMapper.insert(flavor);
            }
        }
    }

    @Override
    public List<DishDto> list(Long categoryId) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> dishList = dishMapper.selectList(queryWrapper);
        List<DishDto> dishDtoList =  new ArrayList<>();
        if(dishList!=null && dishList.size()>0) {
            for (Dish dish : dishList) {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(dish, dishDto);
                LambdaQueryWrapper<DishFlavor> wrapper1 = new LambdaQueryWrapper<>();
                wrapper1.eq(DishFlavor::getDishId, dish.getId());
                List<DishFlavor> dishFlavors = dishFlavorMapper.selectList(wrapper1);

                dishDto.setFlavors(dishFlavors);
                //3.将每一个DishDto对象存入到dishDtoList集合中
                dishDtoList.add(dishDto);
            }
        }
        return dishDtoList;
    }

    @Override
    public void updateStatus(int status, Long[] ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Dish::getId,ids);
        List<Dish> list = dishMapper.selectList(queryWrapper);
        for (Dish dish : list){
            if (dish != null){
                dish.setStatus(status);
                dishMapper.updateById(dish);
            }
        }

    }

    @Override
    public void deleteWithDish(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(Dish::getStatus,1);
        Integer count = dishMapper.selectCount(queryWrapper);
        //方式二：调用菜品业务层接口进行查询(业务比较复杂)
        //dishService.selectCount(xxx);
        //2.如果数据不存在，则直接可以删除分类数据
        if (count > 0) {
            throw new BusinessException("不能删除，正在售卖");
        }
        dishMapper.deleteBatchIds(ids);
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorMapper.delete(lambdaQueryWrapper);
    }


}
