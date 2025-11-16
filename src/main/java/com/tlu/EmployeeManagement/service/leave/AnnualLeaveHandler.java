package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveType;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class AnnualLeaveHandler implements LeaveTypeHandler {
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository; 

    @Override
    public LeaveType getType() {
        return LeaveType.ANNUAL_LEAVE;
    }

    @Override
    public LeaveRequest handle(LeaveRequestCreateDto dto) {
 
        Employee emp = employeeRepository.findById(dto.getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmpId()));

        // Count existing ANNUAL_LEAVE requests for the same calendar year (based on createdAt)
        int year = java.time.LocalDate.now().getYear();
        long existing = leaveRequestRepository.countByEmpIdAndLeaveTypeAndYear(emp.getId(), LeaveType.ANNUAL_LEAVE, year);
        if (existing >= 12) {
            throw new IllegalArgumentException("Annual leave quota exceeded for the year");
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
