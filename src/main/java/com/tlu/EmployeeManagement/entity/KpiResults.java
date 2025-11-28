package com.tlu.EmployeeManagement.entity;

import java.math.BigDecimal;

import com.tlu.EmployeeManagement.enums.RatingEmployeeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "kpi_results")
@Data
@EqualsAndHashCode(callSuper = true)
public class KpiResults extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "kpi_period_id")
    private Integer kpiPeriodId;

    @Column(name = "final_score", precision = 5, scale = 2)
    private BigDecimal finalScore;

    @Column(name = "rating")
    private RatingEmployeeType rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "recorded_by")
    private Integer recordedBy;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private BigDecimal finalScore;
        private Integer kpiPeriodId;
        private RatingEmployeeType rating;
        private String comment;
        private Integer recordedBy;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder kpiPeriodId(Integer kpiPeriodId) {
            this.kpiPeriodId = kpiPeriodId;
            return this;
        }

        public Builder finalScore(BigDecimal finalScore) {
            this.finalScore = finalScore;
            return this;
        }

        public Builder recordedBy(Integer recordedBy) {
            this.recordedBy = recordedBy;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder rating(RatingEmployeeType rating) {
            this.rating = rating;
            return this;
        }

        public KpiResults build() {
            KpiResults kpiResults = new KpiResults();
            kpiResults.empId = this.empId;
            kpiResults.kpiPeriodId = this.kpiPeriodId;
            kpiResults.finalScore = this.finalScore;
            kpiResults.recordedBy = this.recordedBy;
            kpiResults.rating = this.rating;
            kpiResults.comment = this.comment;
            return kpiResults;
        }
    }
}
