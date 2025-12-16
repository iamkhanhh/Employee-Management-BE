package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Data Transfer Object for updating a task's status")
public class TaskStatusUpdateDto {
    @Schema(description = "New status for the task", example = "COMPLETED", required = true)
    @NotNull
    private TaskStatus status;
}
