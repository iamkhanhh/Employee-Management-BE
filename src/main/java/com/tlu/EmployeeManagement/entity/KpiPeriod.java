package com.tlu.EmployeeManagement.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "kpi_periods")
@Data
@EqualsAndHashCode(callSuper = true)
public class KpiPeriod extends AbtractEntity {

    @Column(name = "period_name", length = 50)
    private String periodName;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}
