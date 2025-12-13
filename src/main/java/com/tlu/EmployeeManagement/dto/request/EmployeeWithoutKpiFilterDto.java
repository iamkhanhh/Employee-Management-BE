package com.tlu.EmployeeManagement.dto.request;

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
@Schema(description = "Filter criteria for employees without KPI results")
public class EmployeeWithoutKpiFilterDto {

    @Schema(description = "Filter by month (1-12)", example = "12", minimum = "1", maximum = "12")
    Integer month;

    @Schema(description = "Filter by year", example = "2024")
    Integer year;

    @Schema(description = "Filter by department ID (optional for ADMIN, ignored for department heads)", example = "1")
    Integer deptId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer month;
        private Integer year;
        private Integer deptId;

        public Builder month(Integer month) {
            this.month = month;
            return this;
        }

        public Builder year(Integer year) {
            this.year = year;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public EmployeeWithoutKpiFilterDto build() {
            EmployeeWithoutKpiFilterDto dto = new EmployeeWithoutKpiFilterDto();
            dto.month = this.month;
            dto.year = this.year;
            dto.deptId = this.deptId;
            return dto;
        }
    }
}
