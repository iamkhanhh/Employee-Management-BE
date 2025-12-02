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
@Schema(description = "Overview statistics for dashboard")
public class OverviewStats {

    @Schema(description = "Total number of active employees", example = "150")
    private Long totalEmployees;

    @Schema(description = "Total number of departments", example = "8")
    private Long totalDepartments;

    @Schema(description = "Number of new hires this month", example = "5")
    private Long newHiresThisMonth;

    @Schema(description = "Number of terminated employees this month (staff turnover)", example = "2")
    private Long staffTurnoverThisMonth;
}
