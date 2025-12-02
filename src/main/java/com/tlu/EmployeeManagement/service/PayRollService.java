package com.tlu.EmployeeManagement.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.tlu.EmployeeManagement.dto.response.PayRollResponse;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.Payroll;
import com.tlu.EmployeeManagement.exception.ResourceNotFoundException;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.PayrollRepository;
import com.tlu.EmployeeManagement.util.SecurityUtils;
import com.tlu.EmployeeManagement.enums.PayrollStatus;
import com.tlu.EmployeeManagement.service.S3Service;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.entity.Attendance;
import com.tlu.EmployeeManagement.repository.AttendanceRepository;
import com.tlu.EmployeeManagement.repository.UserRepository;
import com.tlu.EmployeeManagement.enums.UserRole;
import java.util.ArrayList;
import com.tlu.EmployeeManagement.dto.request.PayRollDto;
import com.tlu.EmployeeManagement.dto.request.DepartmentPayrollDto;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime; 
import java.time.YearMonth;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayRollService {
    final PayrollRepository payrollRepository;
    final EmployeeRepository employeeRepository;
    final S3Service s3Service;
    final DepartmentRepository departmentRepository;
    final AttendanceRepository attendanceRepository;
    final UserRepository userRepository;

    @Value("${attendance.workStartTime}")  
    private String workStartTimeConfig;

    @Value("${attendance.workEndTime}")  
    private String workEndTimeConfig;

    private LocalTime getWorkStart() {
        return LocalTime.parse(workStartTimeConfig);
    }

    private LocalTime getWorkEnd() {
        return LocalTime.parse(workEndTimeConfig);
    }

    private BigDecimal calculateDeductionInDays(long minutesLate) {
        if (minutesLate < 5) return BigDecimal.ZERO;
        if (minutesLate <= 15) return BigDecimal.valueOf(0.25);
        if (minutesLate <= 60) return BigDecimal.valueOf(0.5);
        return BigDecimal.ONE;
    }

    public BigDecimal calculateOvertimeBonus(Integer empId) {
        Employee emp = employeeRepository.findById(empId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));
        List<Attendance> attendances = attendanceRepository.findByEmpId(empId);
        if (attendances.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal totalOvertimeHours = BigDecimal.ZERO;
        for (Attendance att : attendances) {
            if (att.getOvertimeHours() != null) {
                totalOvertimeHours = totalOvertimeHours.add(att.getOvertimeHours());
            }
        }
        BigDecimal salaryPerHour = emp.getBasicSalary() != null
            ? emp.getBasicSalary().divide(BigDecimal.valueOf(26 * 8), RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        return totalOvertimeHours.multiply(salaryPerHour).multiply(BigDecimal.valueOf(1.5));
    }

    public BigDecimal calculationDeduction(Integer empId, int month, int year) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + empId));
        List<Attendance> attendances = attendanceRepository.findByEmpIdAndMonthAndYear(empId, month, year);
        BigDecimal totalDeductionDays = BigDecimal.ZERO;
        LocalTime workStart = getWorkStart();
        for (Attendance att : attendances) {
            if (att.getCheckIn() == null) continue;
            LocalTime checkIn = att.getCheckIn().toLocalTime();
            long minutesLate = Duration.between(workStart, checkIn).toMinutes();  
            if (minutesLate > 0) {
                totalDeductionDays = totalDeductionDays.add(calculateDeductionInDays(minutesLate));
            }
        }
        BigDecimal salaryPerDay = emp.getBasicSalary().divide(BigDecimal.valueOf(26), RoundingMode.HALF_UP);
        return salaryPerDay.multiply(totalDeductionDays);
    }

    public PayRollResponse insertPayRoll(PayRollDto dto) {
        Employee emp = employeeRepository.findById(dto.getEmpId())
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + dto.getEmpId()));

        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Current user not authenticated");

        Employee head = employeeRepository.findByUserId(currentUserId)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with userId: " + currentUserId));
        if (!head.getDeptId().equals(emp.getDeptId()) || head.getRoleInDept() != RoleInDepartment.HEAD) {
            throw new RuntimeException("Forbidden: only department head can create payroll for their department");
        }

        Payroll payroll = new Payroll();
        payroll.setEmpId(emp.getId());
        payroll.setAllowance(dto.getAllowance());
        payroll.setBonus(dto.getBonus());
        payroll.setDeduction(dto.getDeduction());

        BigDecimal basic = emp.getBasicSalary() != null ? emp.getBasicSalary() : BigDecimal.ZERO;
        payroll.setStatus(PayrollStatus.PENDING);
        Payroll saved = payrollRepository.save(payroll);
        return toPayRollResponse(saved);
    }

    public List<PayRollResponse> createPayrollByDepartment(DepartmentPayrollDto dto) {
        List<Employee> employees = employeeRepository.findByDeptId(dto.getDeptId());
        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("No employees found for department id: " + dto.getDeptId());
        }
        List<PayRollResponse> responses = new ArrayList<>();
        System.out.println("Creating payroll for department id: " + dto.getDeptId());
        for (Employee emp : employees) {
            System.out.println("Processing employee id: " + emp.getId());
            Payroll payroll = payrollRepository.findLatestByEmpId(emp.getId())
                    .orElseGet(() -> {
                        Payroll p = new Payroll();
                        p.setEmpId(emp.getId());
                        return p;
                    });
            System.out.println(payroll.getId());
            BigDecimal allowance = payroll.getAllowance() != null ? payroll.getAllowance() : BigDecimal.ZERO;
            BigDecimal bonus = calculateOvertimeBonus(emp.getId()).add(
                payroll.getBonus() != null ? payroll.getBonus() : BigDecimal.ZERO
            );
            BigDecimal dedFromAttendance = calculationDeduction(emp.getId(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());
            BigDecimal deduction = (payroll.getDeduction() != null ? payroll.getDeduction() : BigDecimal.ZERO)
                    .add(dedFromAttendance != null ? dedFromAttendance : BigDecimal.ZERO);

            payroll.setAllowance(allowance);
            payroll.setBonus(bonus != null ? bonus : BigDecimal.ZERO);
            payroll.setDeduction(deduction);

            BigDecimal basic = emp.getBasicSalary() != null ? emp.getBasicSalary() : BigDecimal.ZERO;
            BigDecimal netSalary = basic.add(allowance).add(bonus).subtract(deduction);
            payroll.setNetSalary(netSalary);
            payroll.setStatus(PayrollStatus.APPROVED);
            Payroll saved = payrollRepository.save(payroll);
            responses.add(toPayRollResponse(saved));
        }
        return responses;
    }



    public List<PayRollResponse> getPayrollDept(Integer deptId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Current user not authenticated");
        // var userOpt = userRepository.findById(currentUserId);
        // if (userOpt.isEmpty() || userOpt.get().getRole() != UserRole.ADMIN) {
        //     throw new RuntimeException("Forbidden: only admin can view all payrolls");
        // }
        Employee currentEmp = employeeRepository.findByUserId(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Current employee not found"));
        if (!deptId.equals(currentEmp.getDeptId())) {
            throw new RuntimeException("Forbidden: Not head of this department");
        }
        if (currentEmp.getRoleInDept() != RoleInDepartment.HEAD) {
            throw new RuntimeException("Forbidden: Only department head can view summary");
        }
      
        List<Payroll> payrolls = payrollRepository.findPayrollsByDepartment(deptId);
        return payrolls.stream()
                .map(this::toPayRollResponse)
                .collect(Collectors.toList());
    }

    public List<PayRollResponse> filterPayroll(Integer month, Integer year, Integer deptId, String status) {
        PayrollStatus st = null;
        if (status != null && !status.isBlank()) {
            try {
                st = PayrollStatus.valueOf(status.trim());
            } catch (IllegalArgumentException ex) {
                st = null;
            }
        }
        return payrollRepository.filterPayroll(month, year, deptId, st)
                .stream()
                .map(this::toPayRollResponse)
                .collect(Collectors.toList());
    }

    public List<PayRollResponse> getByEmployee(Integer empId) {
        Integer currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null) throw new RuntimeException("Current user not authenticated");
        checkViewPermission(empId, currentUserId);
        return payrollRepository.findByEmpId(empId)
                .stream().map(this::toPayRollResponse).collect(Collectors.toList());
    }

 
    
    private void checkViewPermission(Integer targetEmpId, Integer viewerEmpId) {
        if (targetEmpId.equals(viewerEmpId)) {
            return; 
        }
        Employee targetEmp = employeeRepository.findById(targetEmpId)
                .orElseThrow(() -> new RuntimeException("Target employee not found"));
        Employee viewerEmp = employeeRepository.findByUserId(viewerEmpId)
                .orElseThrow(() -> new RuntimeException("Viewer employee not found"));
        Department targetDept = departmentRepository.findById(targetEmp.getDeptId())
                .orElseThrow(() -> new RuntimeException("Target employee's department not found"));
        Department viewerDept = departmentRepository.findById(viewerEmp.getDeptId())
                .orElseThrow(() -> new RuntimeException("Viewer employee's department not found"));
        if (viewerEmp.getRoleInDept() == RoleInDepartment.HEAD && targetDept.getId().equals(viewerDept.getId())) {
            return;
        }
        throw new RuntimeException("Forbidden: not allowed to view this payroll");
    }

    private PayRollResponse toPayRollResponse(Payroll payroll) {
        Employee emp = employeeRepository.findById(payroll.getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + payroll.getEmpId()));
        
        Department dept = departmentRepository.findById(emp.getDeptId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + emp.getDeptId()));

        PayRollResponse response = new PayRollResponse();
        response.setId(payroll.getId());
        response.setEmpId(payroll.getEmpId());
        response.setEmpName(emp.getFullName());
        response.setEmpPosition(emp.getRoleInDept() != null ? emp.getRoleInDept().name() : null);
        response.setEmpDepartment(dept.getDeptName());
        response.setAllowance(payroll.getAllowance() != null ? payroll.getAllowance().doubleValue() : 0.0);
        response.setBonus(payroll.getBonus() != null ? payroll.getBonus().doubleValue() : 0.0);
        response.setDeduction(payroll.getDeduction() != null ? payroll.getDeduction().doubleValue() : 0.0);
        response.setNetSalary(payroll.getNetSalary() != null ? payroll.getNetSalary().doubleValue() : 0.0);
        response.setStatus(payroll.getStatus().name());
        if (payroll.getFileUrl() != null && !payroll.getFileUrl().isBlank()) {
            try {
                String presigned = s3Service.getS3Url(payroll.getFileUrl());
                response.setFileUrl(presigned);
            } catch (Exception ex) {
                response.setFileUrl(payroll.getFileUrl());
            }
        }
        return response;
    }
}
