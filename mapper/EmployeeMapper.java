package com.itheima.reggie01.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie01.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
    创建持久层接口
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
