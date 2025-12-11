package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "Data transfer object for creating a new task")
public class TaskCreateDto {
    @NotBlank
    @Schema(description = "Task title", example = "Complete project documentation", required = true)
    private String title;

    @Schema(description = "Detailed task description", example = "Create comprehensive documentation for the project including API specs")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Schema(description = "Task due date", example = "31/12/2025", type = "string", pattern = "dd/MM/yyyy")
    private LocalDate dueDate;

    @Schema(description = "List of employee IDs to assign this task to", example = "[1, 2, 3]")
    private List<Integer> employeeIds;

}
