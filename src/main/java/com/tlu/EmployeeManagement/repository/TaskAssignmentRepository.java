package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.TaskAssignment;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {

    Optional<TaskAssignment> findById(Integer id);

    List<TaskAssignment> findByTaskId(Integer taskId);

    List<TaskAssignment> findByEmpId(Integer empId);

    @Query("SELECT ta FROM TaskAssignment ta WHERE ta.empId = :empId AND ta.isDeleted = false")
    List<TaskAssignment> findActiveAssignmentsByEmpId(@Param("empId") Integer empId);

    @Query("SELECT ta FROM TaskAssignment ta WHERE ta.taskId = :taskId AND ta.isDeleted = false")
    List<TaskAssignment> findActiveAssignmentsByTaskId(@Param("taskId") Integer taskId);

    @Query("SELECT ta FROM TaskAssignment ta WHERE ta.empId = :empId AND ta.completedDate IS NULL")
    List<TaskAssignment> findPendingAssignmentsByEmpId(@Param("empId") Integer empId);

    void deleteById(Integer id);
}
