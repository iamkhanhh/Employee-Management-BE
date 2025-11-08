package com.tlu.EmployeeManagement.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "kpi_criteria")
@Data
@EqualsAndHashCode(callSuper = true)
public class KpiCriteria extends AbtractEntity {

    @Column(length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;
}
