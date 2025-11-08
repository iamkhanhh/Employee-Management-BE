package com.tlu.EmployeeManagement.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "kpi_scores")
@Data
@EqualsAndHashCode(callSuper = true)
public class KpiScore extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "kpi_criteria_id")
    private Integer kpiCriteriaId;

    @Column(name = "kpi_period_id")
    private Integer kpiPeriodId;

    @Column(name = "score_value", precision = 5, scale = 2)
    private BigDecimal scoreValue;

    @Column(name = "recorded_by")
    private Integer recordedBy;
}
