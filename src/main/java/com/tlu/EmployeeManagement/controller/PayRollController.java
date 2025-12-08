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
import com.tlu.EmployeeManagement.dto.request.PayRollUpdateDto;
import com.tlu.EmployeeManagement.dto.request.DepartmentPayrollDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ApiResponse<List<PayRollResponse>> createPayroll(
        @RequestParam Integer deptId,
        @Valid @RequestBody List<PayRollDto> dto) {

        List<PayRollResponse> createdList = payRollService.createPayrollByDepartment(deptId,dto);

        return ApiResponse.<List<PayRollResponse>>builder()
                .code(201)
                .status("success")
                .message("Payroll created successfully")
                .data(createdList)
                .build();
    }


    @Operation(summary = "Filter payrolls", description = "Filter payroll records by month, year, department, and status")
    @GetMapping("")
    public ApiResponse<List<PayRollResponse>> filterPayroll(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer deptId,
            @RequestParam(required = false) String status
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

    @Operation(summary = "Update payroll", description = "Update an existing payroll record by its ID")
    @PutMapping("/{payrollId}")
    public ApiResponse<PayRollResponse> updatePayRoll(
            @PathVariable Integer payrollId,
            @Valid @RequestBody PayRollUpdateDto dto) {
            
            return ApiResponse.<PayRollResponse>builder()
                .code(200)
                .status("success")
                .data(payRollService.updatePayRoll(payrollId, dto))
                .build();
    }

}