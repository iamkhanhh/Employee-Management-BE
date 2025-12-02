package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tlu.EmployeeManagement.entity.Employee;

import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.enums.LeaveType;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    Optional<LeaveRequest> findById(Integer id);

    List<LeaveRequest> findByEmpId(Integer empId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.empId = :empId AND lr.status = :status")
    List<LeaveRequest> findByEmpIdAndStatus(@Param("empId") Integer empId, @Param("status") LeaveStatus status);

    List<LeaveRequest> findByLeaveType(LeaveType leaveType);

    // Find all leave requests for employees in a department
    @Query("SELECT lr FROM LeaveRequest lr JOIN com.tlu.EmployeeManagement.entity.Employee e ON lr.empId = e.id WHERE e.deptId = :deptId")
    List<LeaveRequest> findByDepartmentId(@Param("deptId") Integer deptId);

    // Filter leave requests for an employee by optional status and date range
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.empId = :empId " +
           "AND (:status IS NULL OR lr.status = :status) " +
           "AND (:startDate IS NULL OR lr.endDate >= :startDate) " +
           "AND (:endDate IS NULL OR lr.startDate <= :endDate)")
    List<LeaveRequest> findByEmpIdWithFilters(
        @Param("empId") Integer empId,
        @Param("status") LeaveStatus status,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.startDate <= :endDate AND lr.endDate >= :startDate")
    List<LeaveRequest> findLeaveRequestsByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.empId = :empId AND lr.startDate <= :endDate AND lr.endDate >= :startDate")
    List<LeaveRequest> findByEmpIdAndDateRange(
        @Param("empId") Integer empId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COUNT(lr) FROM LeaveRequest lr WHERE lr.empId = :empId AND lr.leaveType = :type AND FUNCTION('YEAR', lr.createdAt) = :year")
    long countByEmpIdAndLeaveTypeAndYear(@Param("empId") Integer empId, @Param("type") LeaveType type, @Param("year") int year);

    void deleteById(Integer id);

    @Query("""
    SELECT COUNT(lr)
    FROM LeaveRequest lr
    JOIN Employee e ON lr.empId = e.id
    WHERE e.deptId = :deptId
  """)
    long countByDept(@Param("deptId") Integer deptId);

    @Query("""
        SELECT COUNT(lr)
        FROM LeaveRequest lr
        JOIN Employee e ON lr.empId = e.id
        WHERE e.deptId = :deptId AND lr.status = :status
    """)
    long countByDeptAndStatus(
            @Param("deptId") Integer deptId,
            @Param("status") LeaveStatus status
    );

}
