package com.tlu.EmployeeManagement.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.EmployeeManagement.dto.request.EmployeeCreateDto;
import com.tlu.EmployeeManagement.dto.request.EmployeeFilterDto;
import com.tlu.EmployeeManagement.dto.request.EmployeeUpdateDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.EmployeeResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Employee", description = "APIs for managing employees")
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {
    EmployeeService employeeService;

    @Operation(summary = "Get all employees", description = "Retrieve a paginated list of employees with optional filtering by status, department, hire date, and name search")
    @GetMapping
    public ApiResponse<PagedResponse<EmployeeResponse>> getEmployees(
            @Parameter(description = "Page number (zero-based)", example = "0") @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @Parameter(description = "Filter by employee status") @RequestParam(required = false) EmployeeStatus status,
            @Parameter(description = "Filter by department ID") @RequestParam(required = false) Integer deptId,
            @Parameter(description = "Filter by hire date (format: dd/MM/yyyy)", example = "01/01/2024") @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate hireDate,
            @Parameter(description = "Search in employee name (case-insensitive)") @RequestParam(required = false) String search) {

        EmployeeFilterDto filterDto = EmployeeFilterDto.builder()
            .page(page)
            .pageSize(pageSize)
            .status(status)
            .deptId(deptId)
            .hireDate(hireDate)
            .search(search)
            .build();

        PagedResponse<EmployeeResponse> employees = employeeService.getEmployees(filterDto);

        ApiResponse<PagedResponse<EmployeeResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get employees successfully");
        apiResponse.setData(employees);
        return apiResponse;
    }

    @Operation(summary = "Get employee by ID", description = "Retrieve a single employee by their ID")
    @GetMapping("/{id}")
    public ApiResponse<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Employee ID", required = true, example = "1") @PathVariable Integer id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get employee successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @Operation(summary = "Create employee", description = "Create a new employee record")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeResponse> createEmployee(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee creation data", required = true)
            @Valid @RequestBody EmployeeCreateDto createDto) {
        EmployeeResponse employee = employeeService.createEmployee(createDto);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee created successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @Operation(summary = "Update employee", description = "Update an existing employee record. All fields are optional for partial updates.")
    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponse> updateEmployee(
            @Parameter(description = "Employee ID", required = true, example = "1") @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee update data", required = true)
            @Valid @RequestBody EmployeeUpdateDto updateDto) {
        EmployeeResponse employee = employeeService.updateEmployee(id, updateDto);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee updated successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @Operation(summary = "Delete employee", description = "Soft delete an employee (sets isDeleted = true)")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteEmployee(
            @Parameter(description = "Employee ID", required = true, example = "1") @PathVariable Integer id) {
        employeeService.deleteEmployee(id);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee deleted successfully");
        return apiResponse;
    }
}
