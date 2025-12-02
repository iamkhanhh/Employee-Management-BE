package com.tlu.EmployeeManagement.dto.request;

import java.math.BigDecimal;

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
public class KpiScoreSubmitDto {

    @NotNull(message = "Criteria ID is required")
    Integer criteriaId;

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
