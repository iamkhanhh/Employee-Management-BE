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

import com.tlu.EmployeeManagement.enums.LeaveType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestCreateDto {

    @NotNull(message = "Employee ID cannot be null")
    Integer empId;

    @NotNull(message = "Leave type cannot be null")
    LeaveType leaveType;

    @NotNull(message = "Start date cannot be null")
    LocalDate startDate;

    @NotNull(message = "End date cannot be null")
    LocalDate endDate;

    @Size(max = 255, message = "Reason must be at most 255 characters")
    String reason;
}
