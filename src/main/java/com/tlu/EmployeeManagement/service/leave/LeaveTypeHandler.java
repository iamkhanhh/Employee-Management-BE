package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveType;



public interface LeaveTypeHandler {
    LeaveType getType();
    LeaveRequest handle(LeaveRequestCreateDto dto);
}
