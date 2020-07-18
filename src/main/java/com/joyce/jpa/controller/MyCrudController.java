package com.joyce.jpa.controller;

import com.joyce.jpa.dao_primary.EmployeeCrudDao;
import com.joyce.jpa.domain_primary.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
@Service
public class MyCrudController {

    @Autowired
    EmployeeCrudDao employeeCrudDao;

    // 查询全部
    @RequestMapping("showall/{page}")
    @ResponseBody
    public Iterable<Employee> findall(@PathVariable("page") String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page),3);
        Iterable<Employee> list = employeeCrudDao.findAll();
        return list;
    }

    @RequestMapping("findByUsername/{username}")
    @ResponseBody
    public Iterable<Employee> findByUsername(@PathVariable("username") String username) {
        Iterable<Employee> list = employeeCrudDao.findByUsername(username);
        return list;
    }

    @RequestMapping("/save")
    @ResponseBody
    public Employee save(String email, String username ) {
        Employee e = new Employee();
        e.setEmail(email);
        e.setUsername(username);
        Employee e2 = employeeCrudDao.save(e);
        return e2;
    }
}
