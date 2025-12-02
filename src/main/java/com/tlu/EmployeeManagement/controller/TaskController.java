package com.tlu.EmployeeManagement.controller;

import com.tlu.EmployeeManagement.dto.request.TaskAssignDto;
import com.tlu.EmployeeManagement.dto.request.TaskCreateDto;
import com.tlu.EmployeeManagement.dto.request.TaskStatusUpdateDto;
import com.tlu.EmployeeManagement.dto.response.TaskResponse;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.service.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Tag(name = "Task", description = "APIs for managing tasks and assignments")
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @Operation(summary = "Create task", description = "Create a new task")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task creation data", required = true)
            @Valid @RequestBody TaskCreateDto dto) {
        TaskResponse created = taskService.createTask(dto);

        return ApiResponse.<TaskResponse>builder()
                .code(201)
                .status("success")
                .message("Task created successfully")
                .data(created)
                .build();
    }

    @Operation(summary = "Assign task to employee", description = "Assign a task to one or more employees")
    @PostMapping("/{taskId}/assign")
    public ApiResponse<TaskResponse> assign(
            @Parameter(description = "Task ID", required = true, example = "1") @PathVariable Integer taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task assignment data", required = true)
            @Valid @RequestBody TaskAssignDto dto) {
        TaskResponse updated = taskService.assignTask(taskId, dto);
        return ApiResponse.<TaskResponse>builder().code(200).status("success").data(updated).build();
    }

    @Operation(summary = "Get current user's tasks", description = "Retrieve all tasks assigned to the currently authenticated user")
    @GetMapping("/me")
    public ApiResponse<List<TaskResponse>> myTasks() {
        Integer userId = SecurityUtils.getCurrentUserId();
        List<TaskResponse> tasks = taskService.getTasksForCurrentUser(userId);
        return ApiResponse.<List<TaskResponse>>builder().code(200).status("success").data(tasks).build();
    }

    @Operation(summary = "Update task status", description = "Update the status of a task (e.g., from IN_PROGRESS to COMPLETED)")
    @PatchMapping("/{taskId}/status")
    public ApiResponse<TaskResponse> updateStatus(
            @Parameter(description = "Task ID", required = true, example = "1") @PathVariable Integer taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Task status update data", required = true)
            @Valid @RequestBody TaskStatusUpdateDto dto) {
        Integer userId = SecurityUtils.getCurrentUserId();
        System.out.println("Updating status for userId: " + userId);
        TaskResponse updated = taskService.updateTaskStatus(taskId, dto.getStatus(), userId);
        return ApiResponse.<TaskResponse>builder().code(200).status("success").data(updated).build();
    }

    @Operation(summary = "Delete task", description = "Soft delete a task (sets isDeleted = true)")
    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(
            @Parameter(description = "Task ID", required = true, example = "1") @PathVariable Integer taskId) {
        Integer userId = SecurityUtils.getCurrentUserId();
        taskService.deleteTask(taskId, userId);
        return ApiResponse.<Void>builder().code(200).status("success").message("Task deleted").build();
    }
}
