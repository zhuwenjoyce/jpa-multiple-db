package com.joyce.jpa.controller;

import com.joyce.jpa.dao_primary.EmployeeJpaDao;
import com.joyce.jpa.domain_primary.Employee;
import com.joyce.jpa.service.EmployeeJpaService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MyJpaController {
    static Logger logger = LoggerFactory.getLogger(MyJpaController.class);
    @Autowired
    EmployeeJpaService employeeJpaService;
    @Autowired
    EmployeeJpaDao employeeJpaDao;

    // 查询全部
    @RequestMapping("/mock-normal")
    @ResponseBody
    public Employee 模拟正常业务(String email, String username ) {
        Employee e = new Employee();
        e.setEmail(email);
        e.setUsername(username);
        Employee saveEmployee = employeeJpaService.模拟正常业务(e);
        return saveEmployee;
    }

    // 模拟业务出错
    @RequestMapping("/mock-error-1") //  localhost:8080/mock-error-1?email=88@qq.com&username=zhu
    @ResponseBody
    public List<Employee> 模拟业务出错_controller层单次调用service测试事务(String email, String username ) {
        Employee e = new Employee();
        e.setEmail(email);
        e.setUsername(username);
        List<Employee> list = new ArrayList<>();
        try {
            Employee saveEmployee = employeeJpaService.模拟业务出错_测试jpa事务_需指定事务(e);
            list.add(saveEmployee);
            return list;
        }catch (Exception ex){
            logger.error("模拟业务出错：："+ ex.getMessage());
            List<Employee> employeeList = employeeJpaDao.findByUsername(username);
            if(CollectionUtils.isNotEmpty(employeeList)){
                employeeList.get(0).setRemark(" from DB, and occurred error! jpa事务没有生效！");
                return employeeList;
            }
            Employee temp = new Employee();
            temp.setRemark("出错了，且数据库中没找到[username="+username+"], jpa事务生效了！");
            list.add(temp);
            return list;
        }
    }

    // 模拟业务出错
    @RequestMapping("/mock-error-2") //  localhost:8080/mock-error-2?email=99@qq.com&username=wen
    @ResponseBody
    public Map<String,Object> 模拟业务出错_controller层多次调用service测试事务(String email, String username ) {
        Employee e = new Employee();
        e.setEmail(email);
        e.setUsername(username);
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            Employee saveEmployee = employeeJpaService.模拟业务出错_测试jpa事务_需指定事务(e);
            map.put("模拟业务出错_测试jpa事务_需指定事务", saveEmployee);
        }catch (Exception ex){
            logger.error("模拟业务出错：："+ ex.getMessage());
            List<Employee> employeeList = employeeJpaDao.findByUsername(username);
            map.put("1-模拟业务出错：：", ex.getMessage());
            map.put("2-模拟业务出错后再到数据库查询数据[username="+username+"]：", employeeList);
        }
        Employee e2 = employeeJpaService.模拟正常业务(e);
        map.put("3-模拟正常业务", e2);
        List<Employee> queryList = employeeJpaDao.findByUsername(username);
        if(CollectionUtils.size(queryList) == 1){
            queryList.get(0).setRemark(" from DB, and occurred error! jpa事务生效了！");
        }else{
            queryList.get(0).setRemark(" from DB, and occurred error! jpa事务没有生效！");
        }
        map.put("4-findByUsername", queryList);
        return map;
    }

}
