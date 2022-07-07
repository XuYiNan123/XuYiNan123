package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie01.mapper.AddressBookMapper;
import com.itheima.reggie01.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class AddressBookServiceImpl implements AddressBookService {
    
    @Autowired
    private AddressBookMapper mapper;
    
    /**
     * 添加地址
     *
     * @param addressBook
     */
    @Override
    public void save(AddressBook addressBook) {
        mapper.insert(addressBook);
    }
    
    /**
     * 把所有的地址都变成普通的地址
     *
     * @param wrapper
     */
    @Override
    public void update(LambdaUpdateWrapper<AddressBook> wrapper) {
        mapper.update(null , wrapper);
    }
    
    /**
     * 根据id，把指定的地址变成默认的地址
     *
     * @param addressBook
     */
    @Override
    public void updateById(AddressBook addressBook) {
        mapper.updateById(addressBook);
    }
    
    /**
     * 根据id查询地址
     *
     * @param id
     * @return
     */
    @Override
    public AddressBook getById(Long id) {
        return mapper.selectById(id);
    }
    
    /**
     * 根据用户id来查询该用户的默认地址
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public AddressBook getOne(LambdaQueryWrapper<AddressBook> queryWrapper) {
        return mapper.selectOne(queryWrapper);
    }
    
    /**
     * 根据用户的id，查询该用户的所有地址
     *
     * @param queryWrapper
     * @return
     */
    @Override
    public List<AddressBook> list(LambdaQueryWrapper<AddressBook> queryWrapper) {
        return mapper.selectList(queryWrapper);
    }
}
