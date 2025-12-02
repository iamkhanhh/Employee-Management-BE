package com.tlu.EmployeeManagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.RatingEmployeeType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KpiResultResponse {

    Integer id;

    Integer empId;

    String empName;

    Integer kpiPeriodId;

    String periodName;

    List<EmployeeReviewScoreDto> scores;

    BigDecimal finalScore;

    RatingEmployeeType rating;

    String comment;

    Integer recordedBy;

    String recordedByName;

    @Schema(description = "KPI result creation timestamp", example = "25/11/2025 16:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdAt;

    @Schema(description = "KPI result last update timestamp", example = "26/11/2025 09:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime updatedAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer empId;
        private String empName;
        private Integer kpiPeriodId;
        private String periodName;
        private List<EmployeeReviewScoreDto> scores;
        private BigDecimal finalScore;
        private RatingEmployeeType rating;
        private String comment;
        private Integer recordedBy;
        private String recordedByName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder empName(String empName) {
            this.empName = empName;
            return this;
        }

        public Builder kpiPeriodId(Integer kpiPeriodId) {
            this.kpiPeriodId = kpiPeriodId;
            return this;
        }

        public Builder periodName(String periodName) {
            this.periodName = periodName;
            return this;
        }

        public Builder scores(List<EmployeeReviewScoreDto> scores) {
            this.scores = scores;
            return this;
        }

        public Builder finalScore(BigDecimal finalScore) {
            this.finalScore = finalScore;
            return this;
        }

        public Builder rating(RatingEmployeeType rating) {
            this.rating = rating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder recordedBy(Integer recordedBy) {
            this.recordedBy = recordedBy;
            return this;
        }

        public Builder recordedByName(String recordedByName) {
            this.recordedByName = recordedByName;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public KpiResultResponse build() {
            KpiResultResponse response = new KpiResultResponse();
            response.id = this.id;
            response.empId = this.empId;
            response.empName = this.empName;
            response.kpiPeriodId = this.kpiPeriodId;
            response.periodName = this.periodName;
            response.scores = this.scores;
            response.finalScore = this.finalScore;
            response.rating = this.rating;
            response.comment = this.comment;
            response.recordedBy = this.recordedBy;
            response.recordedByName = this.recordedByName;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            return response;
        }
    }
}
