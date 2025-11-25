package com.tlu.EmployeeManagement.controller;

import com.tlu.EmployeeManagement.dto.request.TaskAssignDto;
import com.tlu.EmployeeManagement.dto.request.TaskCreateDto;
import com.tlu.EmployeeManagement.dto.request.TaskStatusUpdateDto;
import com.tlu.EmployeeManagement.dto.response.TaskResponse;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.service.TaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ApiResponse<TaskResponse> create(@Valid @RequestBody TaskCreateDto dto) {
        TaskResponse created = taskService.createTask(dto);

        return ApiResponse.<TaskResponse>builder()
                .code(201)
                .status("success")
                .message("Task created successfully")
                .data(created)
                .build();
    }

    @PostMapping("/{taskId}/assign")
    public ApiResponse<TaskResponse> assign(@PathVariable Integer taskId, @Valid @RequestBody TaskAssignDto dto) {
        TaskResponse updated = taskService.assignTask(taskId, dto);
        return ApiResponse.<TaskResponse>builder().code(200).status("success").data(updated).build();
    }

    @GetMapping("/me")
    public ApiResponse<List<TaskResponse>> myTasks() {
        Integer userId = SecurityUtils.getCurrentUserId();
        List<TaskResponse> tasks = taskService.getTasksForCurrentUser(userId);
        return ApiResponse.<List<TaskResponse>>builder().code(200).status("success").data(tasks).build();
    }

    @PatchMapping("/{taskId}/status")
    public ApiResponse<TaskResponse> updateStatus(@PathVariable Integer taskId,
                                                  @Valid @RequestBody TaskStatusUpdateDto dto) {
        Integer userId = SecurityUtils.getCurrentUserId();
        System.out.println("Updating status for userId: " + userId);
        TaskResponse updated = taskService.updateTaskStatus(taskId, dto.getStatus(), userId);
        return ApiResponse.<TaskResponse>builder().code(200).status("success").data(updated).build();
    }

    @DeleteMapping("/{taskId}")
    public ApiResponse<Void> deleteTask(@PathVariable Integer taskId) {
        Integer userId = SecurityUtils.getCurrentUserId();
        taskService.deleteTask(taskId, userId);
        return ApiResponse.<Void>builder().code(200).status("success").message("Task deleted").build();
    }
}
