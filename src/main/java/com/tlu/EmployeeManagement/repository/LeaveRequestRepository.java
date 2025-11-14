package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    void deleteById(Integer id);
}
