package com.tlu.EmployeeManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Performance statistics for the current user")
public class PerformanceStatisticsResponse {

    @Schema(description = "Number of working days (attendance records) this month", example = "20")
    private Integer workingDaysThisMonth;

    @Schema(description = "Number of completed tasks this month", example = "15")
    private Integer completedTasksThisMonth;

    @Schema(description = "Total number of tasks assigned this month", example = "18")
    private Integer totalTasksThisMonth;

    @Schema(description = "Task completion rate as percentage", example = "83.33")
    private Double taskCompletionRate;

    @Schema(description = "Remaining leave days available", example = "5")
    private Integer remainingLeaveDays;

    @Schema(description = "Number of pending leave requests", example = "1")
    private Integer pendingLeaveRequests;

    @Schema(description = "Total overtime hours this month", example = "12.5")
    private Double overtimeHoursThisMonth;
}
