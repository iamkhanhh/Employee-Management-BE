package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.KpiCriteria;

@Repository
public interface KpiCriteriaRepository extends JpaRepository<KpiCriteria, Integer> {

    Optional<KpiCriteria> findById(Integer id);

    @Query("SELECT kc FROM KpiCriteria kc WHERE kc.isDeleted = false ORDER BY kc.weight DESC")
    List<KpiCriteria> findAllActive();

    Optional<KpiCriteria> findByName(String name);

    void deleteById(Integer id);
}
