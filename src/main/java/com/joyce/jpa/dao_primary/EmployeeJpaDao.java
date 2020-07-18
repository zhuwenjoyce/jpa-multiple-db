package com.joyce.jpa.dao_primary;

import com.joyce.jpa.domain_primary.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeJpaDao extends JpaRepository<Employee,Integer> {

    @Query(value = "SELECT u  FROM Employee u WHERE u.username like %:username%")
    List<Employee> findByUsername(@Param("username") String username);

//    Query注解,hql语句
//   适用于查询指定条件的数据
    @Query(value = "select e from Employee e where e.id >= :id order by  e.id desc ")
    Page<Employee> findEmailByIdAndPageable(@Param("id") Integer id, Pageable pageable);

    @Query(value = "SELECT * FROM employee WHERE id > :id " // 这里的employee指的是数据库表名，因为 nativeQuery=true
            , countQuery = "SELECT 1000 FROM dual" // 自定义查询数据总数的sql
            , nativeQuery = true) // nativeQuery=true使用原生sql
    Page<Employee> listEmployeeById(@Param("id") Integer id, Pageable pageable);
}
