package com.tlu.EmployeeManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tlu.EmployeeManagement.dto.request.EmployeeCreateDto;
import com.tlu.EmployeeManagement.dto.request.EmployeeFilterDto;
import com.tlu.EmployeeManagement.dto.request.EmployeeUpdateDto;
import com.tlu.EmployeeManagement.dto.response.EmployeeResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.User;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.repository.UserRepository;
import com.tlu.EmployeeManagement.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Value;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeService {
    final EmployeeRepository employeeRepository;
    final UserRepository userRepository;
    final DepartmentRepository departmentRepository;

    @Value("${leave.annual.default-days}")
    int defaultAnnualLeaveDays;

    public PagedResponse<EmployeeResponse> getEmployees(EmployeeFilterDto filterDto) {
        // Build specification for filtering
        Specification<Employee> spec = EmployeeSpecification.filterEmployees(
            filterDto.getStatus(),
            filterDto.getDeptId(),
            filterDto.getHireDate(),
            filterDto.getSearch()
        );

        // Create pageable with sorting by createdAt descending
        Pageable pageable = PageRequest.of(
            filterDto.getPage(),
            filterDto.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        List<EmployeeResponse> employeeResponses = employeePage.getContent().stream()
            .map(this::toEmployeeResponse)
            .collect(Collectors.toList());

        return PagedResponse.<EmployeeResponse>builder()
            .content(employeeResponses)
            .currentPage(employeePage.getNumber())
            .pageSize(employeePage.getSize())
            .totalElements(employeePage.getTotalElements())
            .totalPages(employeePage.getTotalPages())
            .hasNext(employeePage.hasNext())
            .hasPrevious(employeePage.hasPrevious())
            .build();
    }

    public EmployeeResponse getEmployeeById(Integer id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        if (employee.getIsDeleted()) {
            throw new RuntimeException("Employee has been deleted");
        }

        return toEmployeeResponse(employee);
    }

    public EmployeeResponse createEmployee(EmployeeCreateDto createDto) {
        // Validate user exists
        userRepository.findById(createDto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + createDto.getUserId()));

        // Validate department exists
        departmentRepository.findById(createDto.getDeptId())
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + createDto.getDeptId()));

        Employee employee = new Employee();
        employee.setUserId(createDto.getUserId());
        employee.setDeptId(createDto.getDeptId());
        employee.setFullName(createDto.getFullName());
        employee.setGender(createDto.getGender());
        employee.setDob(createDto.getDob());
        employee.setPhoneNumber(createDto.getPhoneNumber());
        employee.setAddress(createDto.getAddress());
        employee.setHireDate(createDto.getHireDate());
        employee.setStatus(createDto.getStatus() != null ? createDto.getStatus() : EmployeeStatus.ACTIVE);
        employee.setRoleInDept(createDto.getRoleInDept() != null ? createDto.getRoleInDept() : RoleInDepartment.STAFF);
        employee.setAnnualLeaveRemaining(defaultAnnualLeaveDays);
        employee.setIsDeleted(false);

        Employee savedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(savedEmployee);
    }

    public EmployeeResponse updateEmployee(Integer id, EmployeeUpdateDto updateDto) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        if (employee.getIsDeleted()) {
            throw new RuntimeException("Cannot update deleted employee");
        }

        // Validate department if provided
        if (updateDto.getDeptId() != null) {
            departmentRepository.findById(updateDto.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + updateDto.getDeptId()));
            employee.setDeptId(updateDto.getDeptId());
        }

        if (updateDto.getFullName() != null) {
            employee.setFullName(updateDto.getFullName());
        }
        if (updateDto.getGender() != null) {
            employee.setGender(updateDto.getGender());
        }
        if (updateDto.getDob() != null) {
            employee.setDob(updateDto.getDob());
        }
        if (updateDto.getPhoneNumber() != null) {
            employee.setPhoneNumber(updateDto.getPhoneNumber());
        }
        if (updateDto.getAddress() != null) {
            employee.setAddress(updateDto.getAddress());
        }
        if (updateDto.getHireDate() != null) {
            employee.setHireDate(updateDto.getHireDate());
        }
        if (updateDto.getStatus() != null) {
            employee.setStatus(updateDto.getStatus());
        }
        if (updateDto.getRoleInDept() != null) {
            employee.setRoleInDept(updateDto.getRoleInDept());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return toEmployeeResponse(updatedEmployee);
    }

    public void deleteEmployee(Integer id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        // Soft delete
        employee.setIsDeleted(true);
        employeeRepository.save(employee);
    }

    public EmployeeResponse toEmployeeResponse(Employee employee) {
        // Get user information
        String username = null;
        if (employee.getUserId() != null) {
            username = userRepository.findById(employee.getUserId())
                .map(User::getUsername)
                .orElse(null);
        }

        // Get department information
        String departmentName = null;
        if (employee.getDeptId() != null) {
            departmentName = departmentRepository.findById(employee.getDeptId())
                .map(Department::getDeptName)
                .orElse(null);
        }

        return EmployeeResponse.builder()
            .id(employee.getId())
            .fullName(employee.getFullName())
            .gender(employee.getGender() != null ? employee.getGender().name() : null)
            .phoneNumber(employee.getPhoneNumber())
            .department(departmentName)
            .address(employee.getAddress())
            .dob(employee.getDob())
            .hireDate(employee.getHireDate())
            .roleInDept(employee.getRoleInDept() != null ? employee.getRoleInDept().name() : null)
            .annualLeaveRemaining(employee.getAnnualLeaveRemaining())
            .status(employee.getStatus() != null ? employee.getStatus().name() : null)
            .username(username)
            .createdAt(employee.getCreatedAt())
            .build();
    }
}
