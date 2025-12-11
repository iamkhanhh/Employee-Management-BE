package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.LeaveType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for updating an existing leave request")
public class LeaveRequestUpdateDto {

    @Schema(description = "Type of leave requested", example = "ANNUAL_LEAVE", required = true)
    @NotNull
    private LeaveType leaveType;

    @Schema(description = "Leave start date", example = "20/11/2025", type = "string", pattern = "dd/MM/yyyy", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate startDate;

    @Schema(description = "Leave end date", example = "22/11/2025", type = "string", pattern = "dd/MM/yyyy", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate endDate;

    @Schema(description = "Reason for the leave request", example = "Nghỉ phép thăm gia đình")
    private String reason;
}
