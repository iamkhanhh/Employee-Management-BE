package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for assigning an employee to a task")
public class TaskAssignDto {
    @Schema(description = "Employee ID to assign to the task", example = "123", required = true)
    @NotNull
    private Integer employeeId;
}
