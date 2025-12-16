package com.tlu.EmployeeManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data transfer object for creating a leave request")
public class LeaveRequestCreateDto {

    @NotNull(message = "Leave type cannot be null")
    @Schema(description = "Type of leave (ANNUAL_LEAVE/SICK_LEAVE/PERSONAL_LEAVE/UNPAID_LEAVE)", example = "ANNUAL_LEAVE", required = true)
    LeaveType leaveType;

    @NotNull(message = "Start date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Leave start date", example = "01/01/2025", type = "string", pattern = "dd/MM/yyyy", required = true)
    LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Leave end date", example = "05/01/2025", type = "string", pattern = "dd/MM/yyyy", required = true)
    LocalDate endDate;

    @Size(max = 255, message = "Reason must be at most 255 characters")
    @Schema(description = "Reason for leave request", example = "Family emergency", maxLength = 255)
    String reason;
}
