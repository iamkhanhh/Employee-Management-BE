package com.tlu.EmployeeManagement.controller;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.service.LeaveRequestService;
import com.tlu.EmployeeManagement.designpattern.command.CommandFactory;
import com.tlu.EmployeeManagement.designpattern.command.LeaveRequestInvoker;
import com.tlu.EmployeeManagement.dto.request.RejectLeaveDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.LeaveRequestWithEmployeeDto;
import java.util.List;
import java.time.LocalDate;
import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.dto.request.LeaveRequestUpdateDto;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for leave request operations
 * Uses Command Design Pattern for approve and reject operations
 */
@Tag(name = "Leave Requests", description = "APIs for managing employee leave requests")
@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final CommandFactory commandFactory;
    private final LeaveRequestInvoker invoker = new LeaveRequestInvoker();

    public LeaveRequestController(LeaveRequestService leaveRequestService, CommandFactory commandFactory) {
        this.leaveRequestService = leaveRequestService;
        this.commandFactory = commandFactory;
    }

    @Operation(
        summary = "Create a new leave request",
        description = "Creates a new leave request for an employee. The request will be in PENDING status by default."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "201",
        description = "Leave request created successfully",
        content = @Content(schema = @Schema(implementation = LeaveRequest.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Bad request - Invalid input data or validation error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping
    public ApiResponse<LeaveRequest> create(
            @Parameter(description = "Leave request details", required = true)
            @Valid @RequestBody LeaveRequestCreateDto dto) {
        LeaveRequest created = leaveRequestService.createLeaveRequest(dto);

        return ApiResponse.<LeaveRequest>builder()
                .code(201)
                .status("success")
                .message("Leave request created successfully")
                .data(created)
                .build();
    }


    @Operation(
        summary = "Approve a leave request",
        description = "Approves a pending leave request using the Command pattern. " +
                     "Changes the status from PENDING to APPROVED and deducts leave days from employee balance."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Leave request approved successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Bad request - Leave request already processed or invalid state",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Leave request not found",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/{id}/approve")
    public ApiResponse<?> approve(
            @Parameter(description = "Leave request ID", required = true, example = "1")
            @PathVariable Integer id) throws Exception {
        var cmd = commandFactory.createApproveCommand(id);
        invoker.executeCommand(cmd);

        return ApiResponse.builder()
                .code(200)
                .status("success")
                .message("Leave request approved")
                .build();
    }


    @Operation(
        summary = "Reject a leave request",
        description = "Rejects a pending leave request using the Command pattern. " +
                     "Changes the status from PENDING to REJECTED. Optionally accepts a rejection reason."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Leave request rejected successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Bad request - Leave request already processed or invalid state",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Leave request not found",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/{id}/reject")
    public ApiResponse<?> reject(
            @Parameter(description = "Leave request ID", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Rejection details (optional)", required = false)
            @RequestBody(required = false) RejectLeaveDto dto) throws Exception {
        String rejectReason = dto == null ? "" : dto.getRejectReason();
        var cmd = commandFactory.createRejectCommand(id, rejectReason);
        invoker.executeCommand(cmd);

        return ApiResponse.builder()
                .code(200)
                .status("success")
                .message("Leave request rejected")
                .build();
    }

    @GetMapping("/department/{deptId}")
    public ApiResponse<List<LeaveRequestWithEmployeeDto>> listByDepartment(@PathVariable Integer deptId) {
        var list = leaveRequestService.listByDepartment(deptId);
        return ApiResponse.<List<LeaveRequestWithEmployeeDto>>builder()
                .code(200)
                .status("success")
                .data(list)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<LeaveRequest> update(@PathVariable Integer id, @Valid @RequestBody LeaveRequestUpdateDto dto) {
        LeaveRequest updated = leaveRequestService.updateLeaveRequest(id, dto);
        return ApiResponse.<LeaveRequest>builder().code(200).status("success").data(updated).build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable Integer id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ApiResponse.builder().code(204).status("success").message("Deleted").build();
    }

    @GetMapping("/my")
    public ApiResponse<List<LeaveRequest>> myRequests(
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        var list = leaveRequestService.listMyRequests(status, startDate, endDate);
        return ApiResponse.<List<LeaveRequest>>builder().code(200).status("success").data(list).build();
    }

    @GetMapping("/summary/department/{deptId}")
    public ApiResponse<?> getDepartmentLeaveSummary(@PathVariable Integer deptId) {
        var summary = leaveRequestService.getDepartmentLeaveSummary(deptId);
        return ApiResponse.builder()
                .code(200)  
                .status("success")
                .data(summary)
                .build();
    }
}
