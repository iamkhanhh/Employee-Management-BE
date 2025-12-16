package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object containing leave request details with employee information")
public class LeaveRequestWithEmployeeDto {
    @Schema(description = "Unique identifier of the leave request", example = "1")
    private Integer id;

    @Schema(description = "Employee ID who submitted the leave request", example = "123")
    private Integer empId;

    @Schema(description = "Full name of the employee", example = "Nguyễn Văn A")
    private String employeeName;

    @Schema(description = "Type of leave requested", example = "ANNUAL_LEAVE")
    private com.tlu.EmployeeManagement.enums.LeaveType leaveType;

    @Schema(description = "Leave start date", example = "20/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Schema(description = "Leave end date", example = "22/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Schema(description = "Reason for the leave request", example = "Nghỉ phép thăm gia đình")
    private String reason;

    @Schema(description = "Current status of the leave request", example = "PENDING")
    private com.tlu.EmployeeManagement.enums.LeaveStatus status;

    @Schema(description = "Leave request creation timestamp", example = "18/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;
}
