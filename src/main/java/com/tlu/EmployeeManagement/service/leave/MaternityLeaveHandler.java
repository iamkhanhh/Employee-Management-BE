package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveType;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;


@Component
public class MaternityLeaveHandler implements LeaveTypeHandler {
    private static final int MIN_DAYS = 90;

    @Override
    public LeaveType getType() {
        return LeaveType.MATERNITY_LEAVE;
    }

    @Override
    public LeaveRequest handle(LeaveRequestCreateDto dto) {
        int days = (int) ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        if (days < MIN_DAYS) {
            throw new IllegalArgumentException("Maternity leave must be at least " + MIN_DAYS + " days");
        }
        LeaveRequest lr = new LeaveRequest();
        lr.setEmpId(dto.getEmpId());
        lr.setLeaveType(dto.getLeaveType());
        lr.setStartDate(dto.getStartDate());
        lr.setEndDate(dto.getEndDate());
        lr.setReason(dto.getReason());
        return lr;
    }
}
