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

    @Query("SELECT p FROM Payroll p WHERE p.empId = :empId ORDER BY p.createdAt DESC LIMIT 1")
    Optional<Payroll> findLatestByEmpId(@Param("empId") Integer empId);

    List<Payroll> findByStatus(PayrollStatus status);

    @Query("SELECT p FROM Payroll p WHERE p.empId = :empId AND p.isDeleted = false ORDER BY p.createdAt DESC")
    List<Payroll> findActivePayrollsByEmpId(@Param("empId") Integer empId);

    @Query("SELECT p FROM Payroll p WHERE p.empId = :empId AND p.status = :status")
    List<Payroll> findByEmpIdAndStatus(@Param("empId") Integer empId, @Param("status") PayrollStatus status);

    @Query("""
        SELECT p FROM Payroll p
        JOIN Employee e ON p.empId = e.id
        WHERE (:month IS NULL OR FUNCTION('MONTH', p.createdAt) = :month)
        AND (:year IS NULL OR FUNCTION('YEAR', p.createdAt) = :year)
        AND (:deptId IS NULL OR e.deptId = :deptId)
        AND (:status IS NULL OR p.status = :status)
    """)
    List<Payroll> filterPayroll(
            @Param("month") Integer month,
            @Param("year") Integer year,
            @Param("deptId") Integer deptId,
            @Param("status") PayrollStatus status
    );

    void deleteById(Integer id);

    @Query("""
    SELECT p FROM Payroll p
    JOIN Employee e ON p.empId = e.id
    WHERE e.deptId = :deptId
    ORDER BY p.createdAt DESC
""")
    List<Payroll> findPayrollsByDepartment(@Param("deptId") Integer deptId);
}
