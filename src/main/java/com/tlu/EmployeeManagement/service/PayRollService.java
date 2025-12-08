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
import com.tlu.EmployeeManagement.entity.User;
import com.tlu.EmployeeManagement.service.EmailService;;
import java.util.Map;
import java.util.ArrayList;
import com.tlu.EmployeeManagement.dto.request.PayRollDto;
import com.tlu.EmployeeManagement.dto.request.PayRollUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalTime; 
import java.time.YearMonth;
import java.util.HashMap;


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
    final EmailService EmailService;

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

    
    public List<PayRollResponse> createPayrollByDepartment(Integer deptId, List<PayRollDto> dto) {
        String role = SecurityUtils.getCurrentUserRole();
        System.out.println("Current user role: " + role);
        if (!"ADMIN".equals(role) && !"ACCOUNTANT".equals(role)) {
            throw new RuntimeException("Forbidden: Only ADMIN or ACCOUNTANT can update payroll");
        }  
        List<PayRollResponse> responses = new ArrayList<>();
        for (PayRollDto item: dto){
            Employee emp = employeeRepository.findById(item.getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + item.getEmpId()));
            if (!emp.getDeptId().equals(deptId)) {
                throw new RuntimeException("Employee with id " + item.getEmpId() + " does not belong to department " + deptId);
            }
            User user = userRepository.findById(emp.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + emp.getUserId()));
            
            Payroll payroll = new Payroll();
            payroll.setEmpId(item.getEmpId());
            BigDecimal allowance = item.getAllowance() != null ? item.getAllowance() : BigDecimal.ZERO; 
            payroll.setAllowance(allowance);

            BigDecimal dtoBonus = item.getBonus() != null ? item.getBonus() : BigDecimal.ZERO;
            BigDecimal overtimeBonus = calculateOvertimeBonus(emp.getId());
            BigDecimal bonus = dtoBonus.add(overtimeBonus != null ? overtimeBonus : BigDecimal.ZERO);
            payroll.setBonus(bonus);

            BigDecimal dtoDeduction = item.getDeduction() != null ? item.getDeduction() : BigDecimal.ZERO;
            BigDecimal dedFromAttendance = calculationDeduction(emp.getId(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());
            BigDecimal deduction = dtoDeduction.add(dedFromAttendance != null ? dedFromAttendance : BigDecimal.ZERO);
            payroll.setDeduction(deduction);

            BigDecimal basic = emp.getBasicSalary() != null ? emp.getBasicSalary() : BigDecimal.ZERO;
            payroll.setBasicSalary(basic);

            BigDecimal netSalary = basic.add(allowance).add(bonus).subtract(deduction); 
            payroll.setNetSalary(netSalary);

            payroll.setStatus(PayrollStatus.APPROVED);
            Payroll saved = payrollRepository.save(payroll);
            responses.add(toPayRollResponse(saved));

            Map<String, Object> emailData = new HashMap<>();
            emailData.put("empName", emp.getFullName());
            System.out.println("Preparing to send email to: " + emp.getFullName() + " at " + user.getEmail());
            emailData.put("basicSalary", basic);
            emailData.put("allowance", allowance);
            emailData.put("bonus", bonus);
            emailData.put("deduction", deduction);
            emailData.put("netSalary", netSalary);
            emailData.put("month", LocalDateTime.now().getMonthValue());
            emailData.put("year", LocalDateTime.now().getYear());
            emailData.put("empPosition", emp.getRoleInDept() != null ? emp.getRoleInDept().name() : "");
            emailData.put("empDepartment", departmentRepository.findById(emp.getDeptId())
                .map(Department::getDeptName)
                .orElse(""));
           
            EmailService.sendPayrollEmail(user.getEmail(), emailData);
        }
        return responses;
    }

    
  
    public PayRollResponse updatePayRoll(Integer payrollId, PayRollUpdateDto dto) {
        String role = SecurityUtils.getCurrentUserRole();
        if (!"ADMIN".equals(role) && !"ACCOUNTANT".equals(role)) {
            throw new RuntimeException("Forbidden: Only ADMIN or ACCOUNTANT can update payroll");
        }
        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() -> new ResourceNotFoundException("Payroll not found with id: " + payrollId));

        Employee emp = employeeRepository.findById(payroll.getEmpId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + payroll.getEmpId()));
        BigDecimal allowance = dto.getAllowance() != null ? dto.getAllowance() : BigDecimal.ZERO; 

        payroll.setAllowance(allowance);
        BigDecimal dtoBonus = dto.getBonus() != null ? dto.getBonus() : BigDecimal.ZERO;
        BigDecimal overtimeBonus = calculateOvertimeBonus(emp.getId());
        BigDecimal bonus = dtoBonus.add(overtimeBonus != null ? overtimeBonus : BigDecimal.ZERO);
        payroll.setBonus(bonus);


        BigDecimal dtoDeduction = dto.getDeduction() != null ? dto.getDeduction() : BigDecimal.ZERO;
        BigDecimal dedFromAttendance = calculationDeduction(emp.getId(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getYear());
        BigDecimal deduction = dtoDeduction.add(dedFromAttendance != null ? dedFromAttendance : BigDecimal.ZERO);
        payroll.setDeduction(deduction);

        BigDecimal basic = emp.getBasicSalary() != null ? emp.getBasicSalary() : BigDecimal.ZERO;
        BigDecimal netSalary = basic.add(allowance).add(bonus).subtract(deduction);
        payroll.setNetSalary(netSalary);

        payroll.setStatus(PayrollStatus.APPROVED);
        Payroll saved = payrollRepository.save(payroll);
        return toPayRollResponse(saved);
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
        return payrollRepository.findLatestByEmpId(empId)
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
        response.setCreatedAt(payroll.getCreatedAt());
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
