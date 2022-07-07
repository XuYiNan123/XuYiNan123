package com.itheima.reggie01.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie01.common.PageResult;
import com.itheima.reggie01.common.R;
import com.itheima.reggie01.entity.Employee;
import com.itheima.reggie01.exception.BusinessException;
import com.itheima.reggie01.mapper.EmployeeMapper;
import com.itheima.reggie01.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 员工业务逻辑处理类
 */
@Service
@Transactional
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public R<Employee> login(Employee employee, HttpSession session) {
        String username = employee.getUsername();//登录账号
        String password = employee.getPassword();//登录密码
        //1、将页面提交的密码password进行md5加密处理
        //由spring提供工具类对明文密码进行md5加密
        String userInputPassowrd = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库(操作mysql数据库Employee表)
        //2.1构建查询条件实现员工查询
        //方式一：硬编码方式
        //QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        //参数1：数据库表中字段名称
        //queryWrapper.eq("username",username);
        //方式二：推荐方式
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,username);
        Employee ey = employeeMapper.selectOne(lambdaQueryWrapper);
        //3、如果没有查询到则返回登录失败结果
        if(ey == null){
            return R.error("登录失败了：用户名不存在");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        String dbPassword = ey.getPassword();//数据库表的密码
        if(!dbPassword.equals(userInputPassowrd)){
            return R.error("登录失败了：密码错误了");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        Integer status = ey.getStatus();//员工状态1：正常 0：禁用
        if(status == 0){
            return R.error("登录失败了：您的账号已经被禁用了");
        }
        //6、登录成功，将员工存入Session并返回登录成功结果 (后续从session中获取用户信息进行使用
        session.setAttribute("employee",ey);
        return R.success(ey);
    }


    @Override
    public R<String> logout(HttpSession session) {
        //1从session中清除用户信息
        //方式一：
        //session.removeAttribute("employee");
        //方式二：推荐使用
        session.invalidate();
        //2返回退出成功结果
        return R.success("登录成功了");
    }


    @Override
    public R<String> save(Employee employee, HttpSession session) {
        //新增员工 登录用户名重复会抛异常，解决异常处理问题
        //方式一：try catch捕获异常
        //方式二：全局异常捕获（推荐使用）
        //根据登录用户名查询员工表看记录是否存在
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee ey = employeeMapper.selectOne(queryWrapper);
        //如果存在，抛出异常(就会被全局异常处理类进行捕获,下面代码就不会继续执行了)
        if(ey != null){
            //注意 需要抛出自己自定义的异常 为了区别程序本身也会抛出此异常
            throw new BusinessException("新增员工失败了：员工登录用户名已经存在");
        }
        //主键id自动生成的 不需要手动设置
        //页面有的数据，后台直接使用 例如：username  name等
        //密码设置
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //状态设置
        employee.setStatus(1);//1:正常 0:禁用
        //新增员工时间
        //employee.setCreateTime(LocalDateTime.now());//不是前段传入的参数 是后端设置的数据
        //更新员工信息时间
        //employee.setUpdateTime(LocalDateTime.now());
        //谁创建的此员工
        //Employee loginUser = (Employee)session.getAttribute("employee");
        //employee.setCreateUser(loginUser.getId());//当前登录用户
        //employee.setUpdateUser(loginUser.getId());
        employeeMapper.insert(employee);
        return R.success("新增员工成功了");
    }


    @Override
    public R<PageResult> findPage(Long page, Long pageSize,String name) {
        //第1步 构造分页参数
        Page pg = new Page(page,pageSize);
        //第2步 调用分页方法
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //2.1 添加条件
        if(StringUtils.isNotEmpty(name)) {
            lambdaQueryWrapper.eq(Employee::getName, name);
        }
        //调用selectPage查询分页内部将分页结果设置到Page对象中
        employeeMapper.selectPage(pg, lambdaQueryWrapper);
        //第3步 构造一个返回结果给页面即可
        long total = pg.getTotal();//总记录数
        List records = pg.getRecords();//当前页面需要显示的数据
        //3.1构造PageResult
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRecords(records);
        return R.success(pageResult);
    }

    @Override
    public R<String> update(Employee employee, HttpSession session) {
        long id = Thread.currentThread().getId();
        log.debug("EmployeeService:::::::::::线程id:{}",id);
        //设置更新员工时间
        //employee.setUpdateTime(LocalDateTime.now());
        //设置更新用户id
        //Employee loginUser = (Employee)session.getAttribute("employee");
        //employee.setUpdateUser(loginUser.getId());

        //根据员工主键id更新员工信息
        employeeMapper.updateById(employee);
        return R.success("更新员工信息成功了");
    }


    @Override
    public R<Employee> findById(Long id) {
        return R.success(employeeMapper.selectById(id));
    }
}
