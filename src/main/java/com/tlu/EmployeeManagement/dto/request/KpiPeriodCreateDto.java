package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
public class KpiPeriodCreateDto {

    @NotBlank(message = "Period name is required")
    String periodName;

    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "KPI period start date", example = "01/01/2025", type = "string", pattern = "dd/MM/yyyy")
    LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "KPI period end date", example = "31/03/2025", type = "string", pattern = "dd/MM/yyyy")
    LocalDate endDate;

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

        public KpiPeriodCreateDto build() {
            KpiPeriodCreateDto dto = new KpiPeriodCreateDto();
            dto.periodName = this.periodName;
            dto.startDate = this.startDate;
            dto.endDate = this.endDate;
            return dto;
        }
    }
}
