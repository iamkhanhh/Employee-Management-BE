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

    @Schema(description = "KPI Period ID", example = "1", required = true)
    Integer kpiPeriodId;

    @Schema(description = "Filter by department ID (optional for ADMIN, ignored for department heads)", example = "1")
    Integer deptId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer kpiPeriodId;
        private Integer deptId;

        public Builder kpiPeriodId(Integer kpiPeriodId) {
            this.kpiPeriodId = kpiPeriodId;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public EmployeeWithoutKpiFilterDto build() {
            EmployeeWithoutKpiFilterDto dto = new EmployeeWithoutKpiFilterDto();
            dto.kpiPeriodId = this.kpiPeriodId;
            dto.deptId = this.deptId;
            return dto;
        }
    }
}
