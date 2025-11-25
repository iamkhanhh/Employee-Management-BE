package com.tlu.EmployeeManagement.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskAssignDto {
    @NotNull
    private Integer employeeId;
}
