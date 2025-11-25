package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskCreateDto {
    @NotBlank
    private String title;

    private String description;

    private LocalDate dueDate;

}
