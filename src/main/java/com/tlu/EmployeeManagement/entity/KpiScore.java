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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private Integer kpiCriteriaId;
        private Integer kpiPeriodId;
        private BigDecimal scoreValue;
        private Integer recordedBy;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder kpiCriteriaId(Integer kpiCriteriaId) {
            this.kpiCriteriaId = kpiCriteriaId;
            return this;
        }

        public Builder kpiPeriodId(Integer kpiPeriodId) {
            this.kpiPeriodId = kpiPeriodId;
            return this;
        }

        public Builder scoreValue(BigDecimal scoreValue) {
            this.scoreValue = scoreValue;
            return this;
        }

        public Builder recordedBy(Integer recordedBy) {
            this.recordedBy = recordedBy;
            return this;
        }

        public KpiScore build() {
            KpiScore kpiScore = new KpiScore();
            kpiScore.empId = this.empId;
            kpiScore.kpiCriteriaId = this.kpiCriteriaId;
            kpiScore.kpiPeriodId = this.kpiPeriodId;
            kpiScore.scoreValue = this.scoreValue;
            kpiScore.recordedBy = this.recordedBy;
            return kpiScore;
        }
    }
}
