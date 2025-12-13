package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // Dashboard queries
    Long countByIsDeletedAndStatus(boolean isDeleted, EmployeeStatus status);

    Long countByIsDeletedAndHireDateBetween(boolean isDeleted, LocalDate startDate, LocalDate endDate);

    Long countByIsDeletedAndStatusAndUpdatedAtBetween(boolean isDeleted, EmployeeStatus status,
                                                       LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Employee> findByIsDeletedAndStatus(boolean isDeleted, EmployeeStatus status);

    List<Employee> findByIsDeleted(boolean isDeleted);

    void deleteById(Integer id);

    List<Employee> findByDeptId(Integer deptId);

    @Query("SELECT DISTINCT e FROM Employee e " +
           "LEFT JOIN KpiPeriod kp ON (kp.startDate <= :endDate AND kp.endDate >= :startDate AND kp.isDeleted = false) " +
           "LEFT JOIN KpiResults kr ON (e.id = kr.empId AND kr.kpiPeriodId = kp.id AND kr.isDeleted = false) " +
           "WHERE e.isDeleted = false " +
           "AND (:deptId IS NULL OR e.deptId = :deptId) " +
           "AND kr.id IS NULL")
    List<Employee> findEmployeesWithoutKpiResults(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("deptId") Integer deptId
    );

}
