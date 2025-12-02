package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.enums.LeaveType;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UnpaidLeaveHandler implements LeaveTypeHandler {
    private final EmployeeRepository employeeRepository;
    @Override
    public LeaveType getType() {
        return LeaveType.UNPAID_LEAVE;
    }

    @Override
    public LeaveRequest handle(LeaveRequestCreateDto dto) {
    Integer currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) throw new RuntimeException("Unauthenticated");
    Employee emp = employeeRepository.findByUserId(currentUserId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found for current user"));

    LeaveRequest lr = new LeaveRequest();
    lr.setEmpId(emp.getId());
        lr.setLeaveType(dto.getLeaveType());
        lr.setStartDate(dto.getStartDate());
        lr.setEndDate(dto.getEndDate());
        lr.setReason(dto.getReason());
        return lr;
    }
}
