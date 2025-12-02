package com.tlu.EmployeeManagement.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.PayRollResponse;
import com.tlu.EmployeeManagement.service.PayRollService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.tlu.EmployeeManagement.dto.request.PayRollDto;
import com.tlu.EmployeeManagement.dto.request.DepartmentPayrollDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@Tag(name = "Payroll", description = "APIs for managing employee payrolls")
@RestController
@RequestMapping("/payrolls")
@RequiredArgsConstructor
public class PayRollController {
    private final PayRollService payRollService;

    @Operation(summary = "Create payroll for department", description = "Create payroll records for all employees in a specific department")
    @PostMapping
    public ApiResponse<List<PayRollResponse>> createPayroll(@Valid @RequestBody DepartmentPayrollDto dto) {

        List<PayRollResponse> createdList = payRollService.createPayrollByDepartment(dto);

        return ApiResponse.<List<PayRollResponse>>builder()
                .code(201)
                .status("success")
                .message("Payroll created successfully")
                .data(createdList)
                .build();
    }


    @Operation(summary = "Create single payroll", description = "Create a payroll record for a single employee with allowances, bonuses, and deductions")
    @PostMapping("/single")
    public ApiResponse<PayRollResponse> createSinglePayroll(@Valid @RequestBody PayRollDto dto) {
        PayRollResponse created = payRollService.insertPayRoll(dto);
        return ApiResponse.<PayRollResponse>builder()
                .code(201)
                .status("success")
                .message("Payroll created successfully")
                .data(created)
                .build();
    }

    @Operation(summary = "Filter payrolls", description = "Filter payroll records by month, year, department, and status")
    @GetMapping("")
    public ApiResponse<List<PayRollResponse>> filterPayroll(
            @Parameter(description = "Month (1-12)", example = "12") @RequestParam(required = false) Integer month,
            @Parameter(description = "Year", example = "2025") @RequestParam(required = false) Integer year,
            @Parameter(description = "Department ID", example = "1") @RequestParam(required = false) Integer deptId,
            @Parameter(description = "Payroll status", example = "PAID") @RequestParam(required = false) String status
    ) {
        return ApiResponse.<List<PayRollResponse>>builder()
                .code(200)
                .status("success")
                .data(payRollService.filterPayroll(month, year, deptId, status))
                .build();
    }


    @Operation(summary = "Get payrolls by employee", description = "Retrieve all payroll records for a specific employee")
    @GetMapping("/employee/{empId}")
    public ApiResponse<List<PayRollResponse>> getPayrollByEmployee(
            @Parameter(description = "Employee ID", example = "1") @PathVariable Integer empId) {
        return ApiResponse.<List<PayRollResponse>>builder()
                .code(200)
                .status("success")
                .data(payRollService.getByEmployee(empId))
                .build();
    }

    @Operation(summary = "Get payrolls by department", description = "Retrieve all payroll records for a specific department")
    @GetMapping("/department/{deptId}")
    public ApiResponse<List<PayRollResponse>> getPayrollByDepartment(
            @Parameter(description = "Department ID", example = "1") @PathVariable Integer deptId) {
        return ApiResponse.<List<PayRollResponse>>builder()
                .code(200)
                .status("success")
                .data(payRollService.getPayrollDept(deptId))
                .build();
    }


}
