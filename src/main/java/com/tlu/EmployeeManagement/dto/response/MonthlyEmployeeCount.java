package com.tlu.EmployeeManagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Monthly employee count for trend analysis")
public class MonthlyEmployeeCount {

    @Schema(description = "Year and month in format YYYY-MM", example = "2024-01")
    private String month;

    @Schema(description = "Total number of active employees at the end of this month", example = "145")
    private Long employeeCount;
}
