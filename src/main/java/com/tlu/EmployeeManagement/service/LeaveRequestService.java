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
import com.tlu.EmployeeManagement.dto.request.LeaveRequestUpdateDto;
import com.tlu.EmployeeManagement.dto.response.LeaveRequestWithEmployeeDto;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import java.util.ArrayList;
import java.util.List;

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

  
    public List<LeaveRequestWithEmployeeDto> listByDepartment(Integer deptId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Unauthenticated");

        Employee currentEmp = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current employee not found"));

        if (currentEmp.getDeptId() == null || !currentEmp.getDeptId().equals(deptId)) {
            throw new RuntimeException("Forbidden: not head of this department");
        }

        if (currentEmp.getRoleInDept() != RoleInDepartment.HEAD) {
            throw new RuntimeException("Forbidden: only department head can access");
        }

        var leaves = leaveRequestRepository.findByDepartmentId(deptId);
        List<LeaveRequestWithEmployeeDto> dtoList = new ArrayList<>();
        for (LeaveRequest lr : leaves) {
            LeaveRequestWithEmployeeDto dto = new LeaveRequestWithEmployeeDto();
            dto.setId(lr.getId());
            dto.setEmpId(lr.getEmpId());
            dto.setEmployeeName(employeeRepository.findById(lr.getEmpId()).map(Employee::getFullName).orElse(null));
            dto.setLeaveType(lr.getLeaveType());
            dto.setStartDate(lr.getStartDate());
            dto.setEndDate(lr.getEndDate());
            dto.setReason(lr.getReason());
            dto.setStatus(lr.getStatus());
            dto.setCreatedAt(lr.getCreatedAt());
            dtoList.add(dto);
        }
        return dtoList;
    }

    // 2) Update own leave
    @Transactional
    public LeaveRequest updateLeaveRequest(Integer id, LeaveRequestUpdateDto updateDto) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Unauthenticated");

        Employee currentEmp = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current employee not found"));

        LeaveRequest lr = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (!lr.getEmpId().equals(currentEmp.getId())) {
            throw new RuntimeException("Forbidden: only owner can update");
        }

        if (lr.getStatus() == LeaveStatus.APPROVED || lr.getStatus() == LeaveStatus.REJECTED) {
            throw new RuntimeException("Cannot update an approved or rejected request");
        }

        // validate dates
        if (updateDto.getStartDate().isAfter(updateDto.getEndDate())) {
            throw new IllegalArgumentException("startDate must be before or equal endDate");
        }

        lr.setLeaveType(updateDto.getLeaveType());
        lr.setStartDate(updateDto.getStartDate());
        lr.setEndDate(updateDto.getEndDate());
        lr.setReason(updateDto.getReason());

        return leaveRequestRepository.save(lr);
    }

    @Transactional
    public void deleteLeaveRequest(Integer id) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Unauthenticated");

        Employee currentEmp = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current employee not found"));

        LeaveRequest lr = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found"));

        if (!lr.getEmpId().equals(currentEmp.getId())) {
            throw new RuntimeException("Forbidden: only owner can delete");
        }

        if (lr.getStatus() == LeaveStatus.APPROVED || lr.getStatus() == LeaveStatus.REJECTED) {
            throw new RuntimeException("Cannot delete an approved or rejected request");
        }

        leaveRequestRepository.deleteById(id);
    }

    // 3) list my requests with optional filters
    public List<LeaveRequest> listMyRequests(LeaveStatus status, LocalDate startDate, LocalDate endDate) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Unauthenticated");

        Employee currentEmp = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current employee not found"));

        return leaveRequestRepository.findByEmpIdWithFilters(currentEmp.getId(), status, startDate, endDate);
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

    @Transactional
    public LeaveRequest undoApproveLeaveRequest(Integer leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

        // Only allow undo if currently approved
        if (leaveRequest.getStatus() != LeaveStatus.APPROVED) {
            throw new IllegalStateException("Can only undo an approved request");
        }

        // Reset approval fields
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setApprovedBy(null);
        leaveRequest.setApprovedDate(null);
        leaveRequest.setRejectReason(null);

        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest undoRejectLeaveRequest(Integer leaveId) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(leaveId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + leaveId));

        // Only allow undo if currently rejected
        if (leaveRequest.getStatus() != LeaveStatus.REJECTED) {
            throw new IllegalStateException("Can only undo a rejected request");
        }

        // Reset rejection fields
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setApprovedBy(null);
        leaveRequest.setApprovedDate(null);
        leaveRequest.setRejectReason(null);

        return leaveRequestRepository.save(leaveRequest);
    }

    private void checkApproverPermission(LeaveRequest request, Integer approverEmpId) {
        if (approverEmpId == null) throw new RuntimeException("Approver id required");

      
        Employee emp = employeeRepository.findById(request.getEmpId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Department dept = departmentRepository.findById(emp.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        var headOpt = employeeRepository.findFirstByDeptIdAndRoleInDept(dept.getId(), RoleInDepartment.HEAD);
        if (headOpt.isPresent() && headOpt.get().getId().equals(approverEmpId)) {
            return;
        }
        System.out.println("ApproverEmpId: " + approverEmpId);
        System.out.println("HeadOptId: " + headOpt.get().getId());
        throw new RuntimeException("Forbidden: not allowed to approve/reject this leave");
    }

}