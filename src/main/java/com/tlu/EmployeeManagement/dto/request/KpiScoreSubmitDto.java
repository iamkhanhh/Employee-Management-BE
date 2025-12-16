package com.tlu.EmployeeManagement.dto.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data Transfer Object for submitting a score for a specific KPI criteria")
public class KpiScoreSubmitDto {

    @Schema(description = "KPI criteria ID being scored", example = "1", required = true)
    @NotNull(message = "Criteria ID is required")
    Integer criteriaId;

    @Schema(description = "Score value for this criteria (typically 0-10)", example = "9.0", required = true)
    @NotNull(message = "Score value is required")
    BigDecimal scoreValue;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer criteriaId;
        private BigDecimal scoreValue;

        public Builder criteriaId(Integer criteriaId) {
            this.criteriaId = criteriaId;
            return this;
        }

        public Builder scoreValue(BigDecimal scoreValue) {
            this.scoreValue = scoreValue;
            return this;
        }

        public KpiScoreSubmitDto build() {
            KpiScoreSubmitDto dto = new KpiScoreSubmitDto();
            dto.criteriaId = this.criteriaId;
            dto.scoreValue = this.scoreValue;
            return dto;
        }
    }
}
