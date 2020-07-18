package com.joyce.jpa.controller;

import com.joyce.jpa.dao_primary.EmployeeJpaDao;
import com.joyce.jpa.domain_primary.Employee;
import com.joyce.jpa.service.Jpa的4种分页Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class My分页测试Controller {
    static Logger logger = LoggerFactory.getLogger(My分页测试Controller.class);
    @Autowired
    Jpa的4种分页Service jpa的4种分页Service;
    @Autowired
    EmployeeJpaDao employeeJpaDao;

    @RequestMapping("/fenye-1") // http://localhost:8080/fenye-1
    @ResponseBody
    public Page<Employee> 分页方式1() {
        Page<Employee> page = jpa的4种分页Service.分页方式1_自己查询总数自己查询数据最后组装分页对象();
        return page;
    }

    @RequestMapping("/fenye-2") // http://localhost:8080/fenye-2?id=60
    @ResponseBody
    public Page<Employee> 分页方式2(Integer id) {
        Page<Employee> page = jpa的4种分页Service.分页方式2_使用jpa的pageable进行分页(id);
        return page;
    }

    @RequestMapping("/fenye-3") // http://localhost:8080/fenye-3?page=0&size=5
    @ResponseBody
    public Page<Employee> 分页方式3(Integer page, Integer size) {
        Page<Employee> list = jpa的4种分页Service.分页方式3_jpa已经实现的分页方式(page, size);
        return list;
    }

    @RequestMapping("/fenye-4") // http://localhost:8080/fenye-4?page=0&size=5&id=2
    @ResponseBody
    public Page<Employee> 分页方式4(Integer id, Integer page, Integer size) {
        Page<Employee> list = jpa的4种分页Service.分页方式4_自定义分页SQL和总数查询countQuery(id, page, size);
        return list;
    }

}
