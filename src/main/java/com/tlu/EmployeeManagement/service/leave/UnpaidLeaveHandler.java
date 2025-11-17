package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveType;
import org.springframework.stereotype.Component;


@Component
public class UnpaidLeaveHandler implements LeaveTypeHandler {
    @Override
    public LeaveType getType() {
        return LeaveType.UNPAID_LEAVE;
    }

    @Override
    public LeaveRequest handle(LeaveRequestCreateDto dto) {
        LeaveRequest lr = new LeaveRequest();
        lr.setEmpId(dto.getEmpId());
        lr.setLeaveType(dto.getLeaveType());
        lr.setStartDate(dto.getStartDate());
        lr.setEndDate(dto.getEndDate());
        lr.setReason(dto.getReason());
        return lr;
    }
}
