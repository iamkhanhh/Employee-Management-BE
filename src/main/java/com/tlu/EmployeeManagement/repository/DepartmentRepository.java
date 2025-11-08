package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findById(Integer id);

    @Query("SELECT d FROM Department d WHERE d.isDeleted = false")
    List<Department> findAllActive();

    Optional<Department> findByDeptName(String deptName);

    void deleteById(Integer id);
}
