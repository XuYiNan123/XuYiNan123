package com.itheima.reggie01.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    
    /**
     * 添加地址
     * @param addressBook
     */
    void save(AddressBook addressBook);
    
    /**
     * 把所有的地址都变成普通的地址
     * @param wrapper
     */
    void update(LambdaUpdateWrapper<AddressBook> wrapper);
    
    /**
     * 根据id，把指定的地址变成默认的地址
     * @param addressBook
     */
    void updateById(AddressBook addressBook);
    
    /**
     *  根据id查询地址
     * @param id
     * @return
     */
    AddressBook getById(Long id);
    
    /**
     * 根据用户id来查询该用户的默认地址
     * @param queryWrapper
     * @return
     */
    AddressBook getOne(LambdaQueryWrapper<AddressBook> queryWrapper);
    
    
    /**
     * 根据用户的id，查询该用户的所有地址
     * @param queryWrapper
     * @return
     */
    List<AddressBook> list(LambdaQueryWrapper<AddressBook> queryWrapper);
}
