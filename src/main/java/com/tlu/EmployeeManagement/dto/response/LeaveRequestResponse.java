package com.tlu.EmployeeManagement.dto.response;


import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.enums.LeaveType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing leave request details")
public class LeaveRequestResponse {
    @Schema(description = "Unique identifier of the leave request", example = "1")
    Integer id;

    @Schema(description = "Employee ID who submitted the leave request", example = "123")
    Integer empId;

    @Schema(description = "Full name of the employee", example = "Nguyễn Văn A")
    String employeeName;

    @Schema(description = "Type of leave requested", example = "ANNUAL_LEAVE")
    LeaveType leaveType;

    @Schema(description = "Leave start date", example = "20/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate startDate;

    @Schema(description = "Leave end date", example = "22/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate endDate;

    @Schema(description = "Number of days requested", example = "3")
    Integer daysRequested;

    @Schema(description = "Reason for the leave request", example = "Nghỉ phép thăm gia đình")
    String reason;

    @Schema(description = "Current status of the leave request", example = "APPROVED")
    LeaveStatus status;

    @Schema(description = "ID of the approver", example = "456")
    Integer approvedBy;

    @Schema(description = "Full name of the approver", example = "Trần Thị B")
    String approverName;

    @Schema(description = "Leave approval date", example = "19/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate approvedDate;

    @Schema(description = "Reason for rejection if the request was rejected", example = "Không đủ ngày phép")
    String rejectReason;
}
