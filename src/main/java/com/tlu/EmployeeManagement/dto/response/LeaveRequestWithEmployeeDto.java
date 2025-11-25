package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LeaveRequestWithEmployeeDto {
    private Integer id;
    private Integer empId;
    private String employeeName;
    private com.tlu.EmployeeManagement.enums.LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private com.tlu.EmployeeManagement.enums.LeaveStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;
}
