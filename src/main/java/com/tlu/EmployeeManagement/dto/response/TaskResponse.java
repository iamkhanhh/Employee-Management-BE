package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing task details with assignments")
public class TaskResponse {
    @Schema(description = "Unique identifier of the task", example = "1")
    Integer id;

    @Schema(description = "Title of the task", example = "Prepare Q4 Sales Report")
    String title;

    @Schema(description = "Detailed description of the task", example = "Compile and analyze all sales data for Q4 2025")
    String description;

    @Schema(description = "Task due date", example = "30/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dueDate;

    @Schema(description = "ID of the employee who created the task", example = "456")
    Integer createdBy;

    @Schema(description = "Full name of the task creator", example = "Trần Thị B")
    String creatorName;

    @Schema(description = "Current status of the task", example = "IN_PROGRESS")
    String status;

    @Schema(description = "List of employees assigned to this task")
    List<TaskAssignmentResponse> assignments;

    @Schema(description = "Task creation timestamp", example = "15/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDateTime createdAt;
}
