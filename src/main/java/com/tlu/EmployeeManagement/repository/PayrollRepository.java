package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Payroll;
import com.tlu.EmployeeManagement.enums.PayrollStatus;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Integer> {

    Optional<Payroll> findById(Integer id);

    List<Payroll> findByEmpId(Integer empId);

    List<Payroll> findByStatus(PayrollStatus status);

    @Query("SELECT p FROM Payroll p WHERE p.empId = :empId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    List<Payroll> findActivePayrollsByEmpId(@Param("empId") Integer empId);

    @Query("SELECT p FROM Payroll p WHERE p.empId = :empId AND p.status = :status")
    List<Payroll> findByEmpIdAndStatus(@Param("empId") Integer empId, @Param("status") PayrollStatus status);

    void deleteById(Integer id);
}
