package com.tlu.EmployeeManagement.dto.response;

import com.tlu.EmployeeManagement.enums.LeaveStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Summary statistics of leave requests")
public class LeaveSummaryResponse {
    @Schema(description = "Total number of leave requests", example = "100")
    long total;

    @Schema(description = "Number of pending leave requests", example = "25")
    long pending;

    @Schema(description = "Number of approved leave requests", example = "60")
    long approved;

    @Schema(description = "Number of rejected leave requests", example = "15")
    long rejected;
}
