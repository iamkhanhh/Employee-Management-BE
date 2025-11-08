package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.KpiPeriod;

@Repository
public interface KpiPeriodRepository extends JpaRepository<KpiPeriod, Integer> {

    Optional<KpiPeriod> findById(Integer id);

    @Query("SELECT kp FROM KpiPeriod kp WHERE kp.isDeleted = false ORDER BY kp.startDate DESC")
    List<KpiPeriod> findAllActive();

    @Query("SELECT kp FROM KpiPeriod kp WHERE :date BETWEEN kp.startDate AND kp.endDate")
    Optional<KpiPeriod> findByDate(@Param("date") LocalDate date);

    @Query("SELECT kp FROM KpiPeriod kp WHERE kp.endDate >= :currentDate ORDER BY kp.startDate ASC")
    List<KpiPeriod> findUpcomingPeriods(@Param("currentDate") LocalDate currentDate);

    void deleteById(Integer id);
}
