package com.tlu.EmployeeManagement.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.EmployeeManagement.dto.request.NotificationCreateDto;
import com.tlu.EmployeeManagement.dto.request.NotificationFilterDto;
import com.tlu.EmployeeManagement.dto.request.NotificationUpdateDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.NotificationResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Notification", description = "APIs for managing notifications")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    NotificationService notificationService;

    @Operation(summary = "Get all notifications", description = "Retrieve a paginated list of notifications with optional filtering by department and search. Admin can view all notifications, department heads and regular employees can only view notifications from their department.")
    @GetMapping
    public ApiResponse<PagedResponse<NotificationResponse>> getNotifications(
            @Parameter(description = "Page number (zero-based)", example = "0") @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @Parameter(description = "Filter by department ID") @RequestParam(required = false) Integer deptId,
            @Parameter(description = "Search in notification title or content (case-insensitive)") @RequestParam(required = false) String search) {

        NotificationFilterDto filterDto = NotificationFilterDto.builder()
            .page(page)
            .pageSize(pageSize)
            .deptId(deptId)
            .search(search)
            .build();

        PagedResponse<NotificationResponse> notifications = notificationService.getNotifications(filterDto);

        ApiResponse<PagedResponse<NotificationResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get notifications successfully");
        apiResponse.setData(notifications);
        return apiResponse;
    }

    @Operation(summary = "View notifications", description = "Get 10 notifications with offset (no pagination). Admin can view all notifications, regular employees can only view notifications from their department.")
    @GetMapping("/view")
    public ApiResponse<List<NotificationResponse>> viewNotifications(
            @Parameter(description = "Offset for retrieving notifications", example = "0") @RequestParam(required = false, defaultValue = "0") Integer offset) {

        List<NotificationResponse> notifications = notificationService.viewNotifications(offset);

        ApiResponse<List<NotificationResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("View notifications successfully");
        apiResponse.setData(notifications);
        return apiResponse;
    }

    @Operation(summary = "Get notification by ID", description = "Retrieve a single notification by its ID. Users can only view notifications from their department.")
    @GetMapping("/{id}")
    public ApiResponse<NotificationResponse> getNotificationById(
            @Parameter(description = "Notification ID", required = true, example = "1") @PathVariable Integer id) {

        NotificationResponse notification = notificationService.getNotificationById(id);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get notification successfully");
        apiResponse.setData(notification);
        return apiResponse;
    }

    @Operation(summary = "Create notification", description = "Create a new notification. Only admin or department heads can create notifications. Department heads can only create notifications for their own department.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<NotificationResponse> createNotification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Notification creation data", required = true)
            @Valid @RequestBody NotificationCreateDto createDto) {

        NotificationResponse notification = notificationService.createNotification(createDto);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Notification created successfully");
        apiResponse.setData(notification);
        return apiResponse;
    }

    @Operation(summary = "Update notification", description = "Update an existing notification. Only admin or department heads can update notifications. Department heads can only update notifications for their own department.")
    @PutMapping("/{id}")
    public ApiResponse<NotificationResponse> updateNotification(
            @Parameter(description = "Notification ID", required = true, example = "1") @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Notification update data", required = true)
            @Valid @RequestBody NotificationUpdateDto updateDto) {

        NotificationResponse notification = notificationService.updateNotification(id, updateDto);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Notification updated successfully");
        apiResponse.setData(notification);
        return apiResponse;
    }

    @Operation(summary = "Delete notification", description = "Soft delete a notification (sets isDeleted = true). Only admin or department heads can delete notifications. Department heads can only delete notifications for their own department.")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotification(
            @Parameter(description = "Notification ID", required = true, example = "1") @PathVariable Integer id) {

        notificationService.deleteNotification(id);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Notification deleted successfully");
        return apiResponse;
    }
}
