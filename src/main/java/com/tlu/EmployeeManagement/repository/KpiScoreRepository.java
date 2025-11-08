package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.KpiScore;

@Repository
public interface KpiScoreRepository extends JpaRepository<KpiScore, Integer> {

    Optional<KpiScore> findById(Integer id);

    List<KpiScore> findByEmpId(Integer empId);

    List<KpiScore> findByKpiPeriodId(Integer kpiPeriodId);

    @Query("SELECT ks FROM KpiScore ks WHERE ks.empId = :empId AND ks.kpiPeriodId = :periodId")
    List<KpiScore> findByEmpIdAndPeriodId(@Param("empId") Integer empId, @Param("periodId") Integer periodId);

    @Query("SELECT ks FROM KpiScore ks WHERE ks.empId = :empId AND ks.kpiCriteriaId = :criteriaId AND ks.kpiPeriodId = :periodId")
    Optional<KpiScore> findByEmpIdAndCriteriaIdAndPeriodId(
        @Param("empId") Integer empId,
        @Param("criteriaId") Integer criteriaId,
        @Param("periodId") Integer periodId
    );

    @Query("SELECT ks FROM KpiScore ks WHERE ks.kpiPeriodId = :periodId AND ks.isDeleted = false")
    List<KpiScore> findActiveScoresByPeriod(@Param("periodId") Integer periodId);

    void deleteById(Integer id);
}
