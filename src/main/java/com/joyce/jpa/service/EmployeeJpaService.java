package com.joyce.jpa.service;

import com.joyce.jpa.dao_primary.EmployeeJpaDao;
import com.joyce.jpa.domain_primary.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeJpaService {
    @Autowired
    EmployeeJpaDao employeeJpaDao;

//    @Transactional(transactionManager = "masterTransactionManager")
    @Transactional(value = "masterTransactionManager")
    public Employee 模拟正常业务(Employee e){
        Employee e2 = employeeJpaDao.save(e);
        Optional<Employee> findEmployee = employeeJpaDao.findById(e2.getId());
        return findEmployee.get();
    }

//    @Transactional(transactionManager = "masterTransactionManager")
    @Transactional(value = "masterTransactionManager")
    public Employee 模拟业务出错_测试jpa事务_需指定事务(Employee e){
        Employee e2 = employeeJpaDao.save(e);
        int i = 1/0;
        Optional<Employee> findEmployee = employeeJpaDao.findById(e2.getId());
        return findEmployee.get();
    }

    public List<Employee> jpa自带的排序方式(Integer page, Integer size){
        List<Employee> list = employeeJpaDao.findAll(Sort.by("id").descending());
        return list;
    }
}
