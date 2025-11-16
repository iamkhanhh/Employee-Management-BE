package com.tlu.EmployeeManagement.designpattern.command;

import com.tlu.EmployeeManagement.service.LeaveRequestService;
import com.tlu.EmployeeManagement.entity.LeaveRequest;

public class ApproveLeaveCommand implements Command {
    private final LeaveRequestService service;
    private final Integer leaveId;

    public ApproveLeaveCommand(LeaveRequestService service, Integer leaveId) {
        this.service = service;
        this.leaveId = leaveId;
    }

    @Override
    public void execute() throws Exception {
        service.approveLeaveRequest(leaveId);
    }
}
