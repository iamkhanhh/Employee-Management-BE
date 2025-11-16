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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;


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

    @PostMapping
    public ApiResponse<LeaveRequest> create(@Valid @RequestBody LeaveRequestCreateDto dto) {
        LeaveRequest created = leaveRequestService.createLeaveRequest(dto);

        return ApiResponse.<LeaveRequest>builder()
                .code(201)
                .status("success")
                .message("Leave request created successfully")
                .data(created)
                .build();
    }


    @PostMapping("/{id}/approve")
    public ApiResponse<?> approve(@PathVariable Integer id) throws Exception {
        var cmd = commandFactory.createApproveCommand(id);
        invoker.executeCommand(cmd);

        return ApiResponse.builder()
                .code(200)
                .status("success")
                .message("Leave request approved")
                .build();
    }


    @PostMapping("/{id}/reject")
    public ApiResponse<?> reject(@PathVariable Integer id, @RequestBody(required = false) RejectLeaveDto dto) throws Exception {
        String rejectReason = dto == null ? "" : dto.getRejectReason();
        var cmd = commandFactory.createRejectCommand(id, rejectReason);
        invoker.executeCommand(cmd);

        return ApiResponse.builder()
                .code(200)
                .status("success")
                .message("Leave request rejected")
                .build();
}

}
