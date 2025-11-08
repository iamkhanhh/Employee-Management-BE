package com.tlu.EmployeeManagement.entity;

import java.math.BigDecimal;

import com.tlu.EmployeeManagement.enums.PayrollStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "payroll")
@Data
@EqualsAndHashCode(callSuper = true)
public class Payroll extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "basic_salary", precision = 12, scale = 2)
    private BigDecimal basicSalary;

    @Column(precision = 12, scale = 2)
    private BigDecimal allowance;

    @Column(precision = 12, scale = 2)
    private BigDecimal bonus;

    @Column(precision = 12, scale = 2)
    private BigDecimal deduction;

    @Column(name = "net_salary", precision = 12, scale = 2)
    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PayrollStatus status = PayrollStatus.PENDING;

    @Column(name = "file_url", length = 255)
    private String fileUrl;
}
