package com.tlu.EmployeeManagement.designpattern.command;

import org.springframework.stereotype.Component;

import com.tlu.EmployeeManagement.service.LeaveRequestService;

@Component
public class CommandFactory {
    private final LeaveRequestService leaveRequestService;

    public CommandFactory(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    public Command createApproveCommand(Integer leaveId) {
        return new ApproveLeaveCommand(leaveRequestService, leaveId);
    }

    public Command createRejectCommand(Integer leaveId, String rejectReason) {
        return new RejectLeaveCommand(leaveRequestService, leaveId, rejectReason);
    }
}
