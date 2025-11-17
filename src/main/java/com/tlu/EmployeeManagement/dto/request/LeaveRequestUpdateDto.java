package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.tlu.EmployeeManagement.enums.LeaveType;

@Data
public class LeaveRequestUpdateDto {
    @NotNull
    private Integer empId;

    @NotNull
    private LeaveType leaveType;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String reason;
}
