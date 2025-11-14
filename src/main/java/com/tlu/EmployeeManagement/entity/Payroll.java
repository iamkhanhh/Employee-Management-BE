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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private BigDecimal basicSalary;
        private BigDecimal allowance;
        private BigDecimal bonus;
        private BigDecimal deduction;
        private BigDecimal netSalary;
        private PayrollStatus status;
        private String fileUrl;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder basicSalary(BigDecimal basicSalary) {
            this.basicSalary = basicSalary;
            return this;
        }

        public Builder allowance(BigDecimal allowance) {
            this.allowance = allowance;
            return this;
        }

        public Builder bonus(BigDecimal bonus) {
            this.bonus = bonus;
            return this;
        }

        public Builder deduction(BigDecimal deduction) {
            this.deduction = deduction;
            return this;
        }

        public Builder netSalary(BigDecimal netSalary) {
            this.netSalary = netSalary;
            return this;
        }

        public Builder status(PayrollStatus status) {
            this.status = status;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Payroll build() {
            Payroll payroll = new Payroll();
            payroll.empId = this.empId;
            payroll.basicSalary = this.basicSalary;
            payroll.allowance = this.allowance;
            payroll.bonus = this.bonus;
            payroll.deduction = this.deduction;
            payroll.netSalary = this.netSalary;
            payroll.status = this.status != null ? this.status : PayrollStatus.PENDING;
            payroll.fileUrl = this.fileUrl;
            return payroll;
        }
    }
}
