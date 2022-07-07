package com.itheima.reggie01.controller;

import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Dish;
import com.itheima.reggie01.entity.Employee;
import com.itheima.reggie01.entity.Setmeal;
import com.itheima.reggie01.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;

/**
 * 员工管理控制层
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session){
        return employeeService.login(employee,session);
    }


    @PostMapping("/logout")
    public R<String> logout(HttpSession session){
         return employeeService.logout(session);
    }


    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpSession session){
        return employeeService.save(employee,session);
    }


    @GetMapping("/page")
    public R<PageResult> findPage(@RequestParam(value = "page",defaultValue = "1") Long page,@RequestParam(value = "pageSize",defaultValue = "1") Long pageSize,String name){
        return employeeService.findPage(page,pageSize,name);
    }


    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpSession session){
        long id = Thread.currentThread().getId();
        log.debug("EmployeeController:::::::::::线程id:{}",id);
        return employeeService.update(employee,session);
    }


    @GetMapping("/{id}")
    public R<Employee> findById(@PathVariable("id") Long id ){
        return employeeService.findById(id);
    }



}
