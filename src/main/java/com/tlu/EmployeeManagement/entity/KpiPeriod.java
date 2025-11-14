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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String periodName;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder periodName(String periodName) {
            this.periodName = periodName;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public KpiPeriod build() {
            KpiPeriod kpiPeriod = new KpiPeriod();
            kpiPeriod.periodName = this.periodName;
            kpiPeriod.startDate = this.startDate;
            kpiPeriod.endDate = this.endDate;
            return kpiPeriod;
        }
    }
}
