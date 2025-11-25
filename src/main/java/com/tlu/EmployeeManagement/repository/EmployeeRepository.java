package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;

import com.tlu.EmployeeManagement.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findById(Integer id);

    Optional<Employee> findByUserId(Integer userId);

    long countByDeptId(Integer deptId);

    Optional<Employee> findFirstByDeptIdAndRoleInDept(Integer deptId, RoleInDepartment roleInDept);

    List<Employee> findByDeptIdAndStatus(Integer deptId, EmployeeStatus status);

    void deleteById(Integer id);
}
