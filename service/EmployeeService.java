package com.itheima.reggie01.service;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Employee;

import javax.servlet.http.HttpSession;

/**
 * 员工业务处理接口
 */
public interface EmployeeService {

    R<Employee> login(Employee employee, HttpSession session);

    R<String> logout(HttpSession session);

    R<String> save(Employee employee, HttpSession session);

    R<PageResult> findPage(Long page, Long pageSize,String name);

    R<String> update(Employee employee, HttpSession session);

    R<Employee> findById(Long id);
}
