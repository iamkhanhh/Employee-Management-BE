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

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeController {
    EmployeeService employeeService;

    @GetMapping
    public ApiResponse<PagedResponse<EmployeeResponse>> getEmployees(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) Integer deptId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate hireDate,
            @RequestParam(required = false) String search) {

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

    @GetMapping("/{id}")
    public ApiResponse<EmployeeResponse> getEmployeeById(@PathVariable Integer id) {
        EmployeeResponse employee = employeeService.getEmployeeById(id);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get employee successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeCreateDto createDto) {
        EmployeeResponse employee = employeeService.createEmployee(createDto);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee created successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeResponse> updateEmployee(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeUpdateDto updateDto) {
        EmployeeResponse employee = employeeService.updateEmployee(id, updateDto);

        ApiResponse<EmployeeResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee updated successfully");
        apiResponse.setData(employee);
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    // @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee deleted successfully");
        return apiResponse;
    }
}
