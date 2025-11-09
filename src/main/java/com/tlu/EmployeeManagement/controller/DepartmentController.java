package com.tlu.EmployeeManagement.controller;

import com.tlu.EmployeeManagement.dto.request.DepartmentDto;
import com.tlu.EmployeeManagement.dto.response.DepartmentResponse;
import com.tlu.EmployeeManagement.service.DepartmentService;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentController {

    DepartmentService departmentService;

    @GetMapping
    public ApiResponse<List<DepartmentResponse>> getAllDepartments() {
        List<DepartmentResponse> departments = departmentService.getAllDepartments();

        return ApiResponse.<List<DepartmentResponse>>builder()
                .status("success")
                .message("Get all departments successfully")
                .data(departments)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<DepartmentResponse> getDepartmentById(@PathVariable Integer id) {
        DepartmentResponse department = departmentService.getDepartmentById(id);

        return ApiResponse.<DepartmentResponse>builder()
                .status("success")
                .message("Get department successfully")
                .data(department)
                .build();
    }

   
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<DepartmentResponse> createDepartment(@Valid @RequestBody DepartmentDto createDto) {
        DepartmentResponse department = departmentService.createDepartment(createDto);

        return ApiResponse.<DepartmentResponse>builder()
                .status("success")
                .message("Department created successfully")
                .data(department)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<DepartmentResponse> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody DepartmentDto updateDto) {

        DepartmentResponse department = departmentService.updateDepartment(id, updateDto);

        return ApiResponse.<DepartmentResponse>builder()
                .status("success")
                .message("Department updated successfully")
                .data(department)
                .build();
    }


    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);

        return ApiResponse.<Void>builder()
                .status("success")
                .message("Department deleted successfully")
                .build();
    }
}
