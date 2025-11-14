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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;
        private BigDecimal weight;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public KpiCriteria build() {
            KpiCriteria kpiCriteria = new KpiCriteria();
            kpiCriteria.name = this.name;
            kpiCriteria.description = this.description;
            kpiCriteria.weight = this.weight;
            return kpiCriteria;
        }
    }
}
