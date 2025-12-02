package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.KpiResults;

@Repository
public interface KpiResultsRepository extends JpaRepository<KpiResults, Integer> {

    Optional<KpiResults> findById(Integer id);

    @Query("SELECT kr FROM KpiResults kr WHERE kr.kpiPeriodId = :periodId AND kr.isDeleted = false")
    List<KpiResults> findByPeriodId(@Param("periodId") Integer periodId);

    @Query("SELECT kr FROM KpiResults kr WHERE kr.empId = :empId AND kr.kpiPeriodId = :periodId AND kr.isDeleted = false")
    Optional<KpiResults> findByEmpIdAndPeriodId(@Param("empId") Integer empId, @Param("periodId") Integer periodId);

    @Query("SELECT kr FROM KpiResults kr WHERE kr.empId = :empId AND kr.isDeleted = false ORDER BY kr.createdAt DESC")
    List<KpiResults> findByEmpId(@Param("empId") Integer empId);

    void deleteById(Integer id);
}
