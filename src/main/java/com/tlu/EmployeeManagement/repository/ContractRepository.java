package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Contract;
import com.tlu.EmployeeManagement.enums.ContractStatus;
import com.tlu.EmployeeManagement.enums.ContractType;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Integer>, JpaSpecificationExecutor<Contract> {

    Optional<Contract> findById(Integer id);

    List<Contract> findByEmpId(Integer empId);

    @Query("SELECT c FROM Contract c WHERE c.empId = :empId AND c.isDeleted = false")
    List<Contract> findActiveContractsByEmpId(@Param("empId") Integer empId);

    List<Contract> findByStatus(ContractStatus status);

    List<Contract> findByContractType(ContractType contractType);

    @Query("SELECT c FROM Contract c WHERE c.endDate < :date AND c.status = :status")
    List<Contract> findExpiringContracts(@Param("date") LocalDate date, @Param("status") ContractStatus status);

    void deleteById(Integer id);
}
