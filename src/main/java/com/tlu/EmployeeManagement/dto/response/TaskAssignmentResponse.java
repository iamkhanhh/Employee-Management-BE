package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDateTime;

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
@Schema(description = "Response object containing task assignment details for an employee")
public class TaskAssignmentResponse {
    @Schema(description = "Unique identifier of the task assignment", example = "1")
    Integer id;

    @Schema(description = "Employee ID assigned to the task", example = "123")
    Integer empId;

    @Schema(description = "Full name of the assigned employee", example = "Nguyễn Văn A")
    String employeeName;

    @Schema(description = "Task assignment timestamp", example = "20/11/2025 10:30:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime assignedDate;

    @Schema(description = "Task completion timestamp", example = "25/11/2025 16:45:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime completedDate;
}
