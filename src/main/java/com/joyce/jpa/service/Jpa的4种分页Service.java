package com.joyce.jpa.service;

import com.alibaba.fastjson.JSONObject;
import com.joyce.jpa.SpringbootJpaApplication;
import com.joyce.jpa.dao_primary.EmployeeJpaDao;
import com.joyce.jpa.domain_primary.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class Jpa的4种分页Service {
    static Logger logger = LoggerFactory.getLogger(Jpa的4种分页Service.class);

    @Autowired
    EmployeeJpaDao employeeJpaDao;

    public Page<Employee> 分页方式1_自己查询总数自己查询数据最后组装分页对象(){
        Pageable pageable = PageRequest.of(1, 5); // 第1页，每页显示5条
        int count总共几条 = 50;
        List<Employee> employeeList从数据库查出来的当页数据 = Stream.of(
                new Employee().setId(100),
                new Employee().setId(200),
                new Employee().setId(300),
                new Employee().setId(400),
                new Employee().setId(500)
        ).collect(Collectors.toList());
        Page<Employee> page = new PageImpl<Employee>(employeeList从数据库查出来的当页数据, pageable, count总共几条);
        logger.info("分页方式一：" + JSONObject.toJSON(page));
        return page;
    }

    public Page<Employee> 分页方式2_使用jpa的pageable进行分页(Integer id){
        Pageable pageable = PageRequest.of(0, 5); // 第1页，每页显示5条, 这里的索引0是第1页
        Page<Employee> page = employeeJpaDao.findEmailByIdAndPageable(id, pageable);
        return page;
    }

    public Page<Employee> 分页方式3_jpa已经实现的分页方式(Integer page, Integer size){
        Pageable pageable = PageRequest.of(0, 5); // 第1页，每页显示5条, 这里的索引0是第1页
        pageable = PageRequest.of(page, size);
        Page<Employee> list = employeeJpaDao.findAll(pageable);
        return list;
    }

    public Page<Employee> 分页方式4_自定义分页SQL和总数查询countQuery(Integer id, Integer page, Integer size){
        Pageable pageable = PageRequest.of(0, 5); // 第1页，每页显示5条, 这里的索引0是第1页
        pageable = PageRequest.of(page, size);
        Page<Employee> list = employeeJpaDao.listEmployeeById(id, pageable);
        return list;
    }
}
