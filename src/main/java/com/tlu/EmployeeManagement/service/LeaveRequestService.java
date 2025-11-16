package com.tlu.EmployeeManagement.service;


import com.tlu.EmployeeManagement.dto.request.LeaveRequestCreateDto;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.LeaveRequest;
import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.enums.LeaveType;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.service.leave.LeaveTypeHandlerFactory;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.LeaveRequestRepository;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequestService {
    final LeaveRequestRepository leaveRequestRepository;
    final EmployeeRepository employeeRepository;
    final DepartmentRepository departmentRepository;
    final LeaveTypeHandlerFactory handlerFactory;

    @Value("${leave.annual.default-days}")
    int defaultAnnualLeaveDays;

    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequestCreateDto requestDto) {
    var handler = handlerFactory.getHandler(requestDto.getLeaveType());
    LeaveRequest lr = handler.handle(requestDto);
    lr.setStatus(LeaveStatus.PENDING);
    return leaveRequestRepository.save(lr);
    }

    @Transactional
    public LeaveRequest approveLeaveRequest(Integer leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

    if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request is not in pending status");
        }

      
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Current user not authenticated");

        Employee approver = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user id: " + currentUserId));
        Integer approverEmpId = approver.getId();

        
        checkApproverPermission(leaveRequest, approverEmpId);

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setApprovedBy(approverEmpId);
        leaveRequest.setApprovedDate(LocalDate.now());

    
        return leaveRequestRepository.save(leaveRequest);
    }


    @Transactional
    public LeaveRequest rejectLeaveRequest(Integer leaveId, String rejectReason) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

    if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Leave request is not in pending status");
        }

        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Current user not authenticated");

        Employee approver = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for user id: " + currentUserId));
        Integer approverEmpId = approver.getId();

        checkApproverPermission(leaveRequest, approverEmpId);

    leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setApprovedBy(approverEmpId);
        leaveRequest.setApprovedDate(LocalDate.now());
        leaveRequest.setRejectReason(rejectReason);

        if (leaveRequest.getLeaveType() == LeaveType.ANNUAL_LEAVE) {
           
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    private void checkApproverPermission(LeaveRequest request, Integer approverEmpId) {
        if (approverEmpId == null) throw new RuntimeException("Approver id required");

      
        Employee emp = employeeRepository.findById(request.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Department dept = departmentRepository.findById(emp.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        var headOpt = employeeRepository.findFirstByDeptIdAndRoleInDept(dept.getId(), com.tlu.EmployeeManagement.enums.RoleInDepartment.HEAD);
        if (headOpt.isPresent() && headOpt.get().getId().equals(approverEmpId)) {
            return;
        }
        System.out.println("ApproverEmpId: " + approverEmpId);
        System.out.println("HeadOptId: " + headOpt.get().getId());
        throw new RuntimeException("Forbidden: not allowed to approve/reject this leave");
    }

}