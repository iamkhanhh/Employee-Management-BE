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

@RestController
@RequestMapping("/payrolls")
@RequiredArgsConstructor
public class PayRollController {
    private final PayRollService payRollService;

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

    // @PutMapping()
    // public ApiResponse<List<PayRollResponse>> createPayrollUpdate(@Valid @RequestBody PayRollDto dto) {
    //     PayRollResponse updated = payRollService.updatePayRoll(dto);
    //       return ApiResponse.<PayRollResponse>builder()
    //             .code(201)
    //             .status("success")
    //             .message("Payroll updated  successfully")
    //             .data(updated)
    //             .build();
    // }
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


    @GetMapping("/employee/{empId}")
    public ApiResponse<List<PayRollResponse>> getPayrollByEmployee(@PathVariable Integer empId) {
        return ApiResponse.<List<PayRollResponse>>builder()
                .code(200)
                .status("success")
                .data(payRollService.getByEmployee(empId))
                .build();
    }

    @GetMapping("/department/{deptId}")
    public ApiResponse<List<PayRollResponse>> getPayrollByDepartment(@PathVariable Integer deptId) {
        return ApiResponse.<List<PayRollResponse>>builder()
                .code(200)
                .status("success")
                .data(payRollService.getPayrollDept(deptId))
                .build();   
    }


}
