package com.tlu.EmployeeManagement.designpattern.command;

import com.tlu.EmployeeManagement.service.LeaveRequestService;

public class RejectLeaveCommand implements Command {
    private final LeaveRequestService service;
    private final Integer leaveId;
    private final String rejectReason;

    public RejectLeaveCommand(LeaveRequestService service, Integer leaveId, String rejectReason) {
        this.service = service;
        this.leaveId = leaveId;
        this.rejectReason = rejectReason;
    }

    @Override
    public void execute() throws Exception {
        service.rejectLeaveRequest(leaveId, rejectReason);
    }
}
