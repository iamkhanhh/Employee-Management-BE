package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Schema(description = "Filter criteria for searching and filtering KPI evaluation periods")
public class KpiPeriodFilterDto {

    @Schema(description = "Page number for pagination (zero-based)", example = "0")
    Integer page = 0;

    @Schema(description = "Number of items per page", example = "10")
    Integer pageSize = 10;

    @Schema(description = "Search by period name", example = "Q1 2025")
    String periodName;

    @Schema(description = "Filter by start date", example = "01/01/2025", type = "string", pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate startDate;

    @Schema(description = "Filter by end date", example = "31/03/2025", type = "string", pattern = "dd/MM/yyyy")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate endDate;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private String periodName;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

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

        public KpiPeriodFilterDto build() {
            KpiPeriodFilterDto dto = new KpiPeriodFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.periodName = this.periodName;
            dto.startDate = this.startDate;
            dto.endDate = this.endDate;
            return dto;
        }
    }
}
