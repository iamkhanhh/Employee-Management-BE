package com.tlu.EmployeeManagement.entity;

import java.time.LocalDate;

import com.tlu.EmployeeManagement.enums.ContractStatus;
import com.tlu.EmployeeManagement.enums.ContractType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "contracts")
@Data
@EqualsAndHashCode(callSuper = true)
public class Contract extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", length = 50)
    private ContractType contractType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ContractStatus status = ContractStatus.ACTIVE;
}
