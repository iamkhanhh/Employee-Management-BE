package com.tlu.EmployeeManagement.dto.response;

import java.math.BigDecimal;

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
@Schema(description = "Response object containing score details for a specific KPI criteria in an employee review")
public class EmployeeReviewScoreDto {

    @Schema(description = "KPI criteria ID", example = "1")
    Integer criteriaId;

    @Schema(description = "Name of the KPI criteria", example = "Work Quality")
    String criteriaName;

    @Schema(description = "Weight/importance of this criteria (0-1)", example = "0.30")
    BigDecimal weight;

    @Schema(description = "Score value for this criteria", example = "4.0")
    BigDecimal scoreValue;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer criteriaId;
        private String criteriaName;
        private BigDecimal weight;
        private BigDecimal scoreValue;

        public Builder criteriaId(Integer criteriaId) {
            this.criteriaId = criteriaId;
            return this;
        }

        public Builder criteriaName(String criteriaName) {
            this.criteriaName = criteriaName;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
            return this;
        }

        public Builder scoreValue(BigDecimal scoreValue) {
            this.scoreValue = scoreValue;
            return this;
        }

        public EmployeeReviewScoreDto build() {
            EmployeeReviewScoreDto dto = new EmployeeReviewScoreDto();
            dto.criteriaId = this.criteriaId;
            dto.criteriaName = this.criteriaName;
            dto.weight = this.weight;
            dto.scoreValue = this.scoreValue;
            return dto;
        }
    }
}
