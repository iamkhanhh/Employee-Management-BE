package com.tlu.EmployeeManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tlu.EmployeeManagement.dto.request.DepartmentDto;
import com.tlu.EmployeeManagement.dto.response.DepartmentResponse;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;
import com.tlu.EmployeeManagement.dto.response.DepartmentSummaryDto;


import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {

    DepartmentRepository departmentRepository;
    EmployeeRepository employeeRepository;

    public DepartmentResponse createDepartment(DepartmentDto createDto) {

        departmentRepository.findByDeptName(createDto.getDeptName())
            .ifPresent(d -> { throw new RuntimeException("Department name already exists"); });

        if (departmentRepository.existsByEmployeeId(createDto.getEmployeeId())) {
            throw new RuntimeException("Employee is already assigned to a department");
        }

        Department department = new Department();
        department.setDeptName(createDto.getDeptName());
        department.setEmployeeId(createDto.getEmployeeId());
        department.setIsDeleted(false);

    
        employeeRepository.findById(createDto.getEmployeeId()).ifPresent(emp -> {
            emp.setRoleInDept(emp.getRoleInDept());
            employeeRepository.save(emp);
        });

        Department saved = departmentRepository.save(department);
        return toDepartmentResponse(saved);
    }

    public DepartmentResponse updateDepartment(Integer id, DepartmentDto updateDto) {

        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        if (department.getIsDeleted()) {
            throw new RuntimeException("Cannot update deleted department");
        }

        departmentRepository.findByDeptName(updateDto.getDeptName())
            .filter(d -> !d.getId().equals(id))
            .ifPresent(d -> { throw new RuntimeException("Department name already exists"); });

        if (departmentRepository.existsByEmployeeId(updateDto.getEmployeeId())) {
            throw new RuntimeException("Employee is already assigned to a department");
        }

        Integer oldEmployeeId = department.getEmployeeId();

        department.setDeptName(updateDto.getDeptName());
        department.setEmployeeId(updateDto.getEmployeeId());

        // Nếu đổi trưởng phòng, cập nhật role
        if (!oldEmployeeId.equals(updateDto.getEmployeeId())) {

            // Employee cũ → STAFF
            employeeRepository.findById(oldEmployeeId).ifPresent(emp -> {
                emp.setRoleInDept(RoleInDepartment.STAFF);
                employeeRepository.save(emp);
            });

            // Employee mới → HEAD
            employeeRepository.findById(updateDto.getEmployeeId()).ifPresent(emp -> {
                emp.setRoleInDept(RoleInDepartment.HEAD);
                employeeRepository.save(emp);
            });
        }

        Department updated = departmentRepository.save(department);
        return toDepartmentResponse(updated);
    }

    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        department.setIsDeleted(true);
        departmentRepository.save(department);
    }

    public DepartmentResponse getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        return toDepartmentResponse(department);
    }

    // public List<DepartmentResponse> getAllDepartments() {
    //     return departmentRepository.findByIsDeletedFalse().stream()
    //             .map(this::toDepartmentResponse)
    //             .collect(Collectors.toList());
    // }

    public List<DepartmentSummaryDto> getDepartmentSummaries() {
        return departmentRepository.findByIsDeletedFalse().stream()
                .map(dept -> {
                    Long count = employeeRepository.countByDeptId(dept.getId());
                    String managerName = null;
                    if (dept.getEmployeeId() != null) {
                        managerName = employeeRepository.findById(dept.getEmployeeId())
                                .map(Employee::getFullName)
                                .orElse(null);
                    }
                    return new DepartmentSummaryDto(dept.getId(), dept.getDeptName(), managerName, count);
                })
                .collect(Collectors.toList());
    }

    private DepartmentResponse toDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .deptName(department.getDeptName())
                .employeeId(department.getEmployeeId())
                .createdAt(department.getCreatedAt())
                .build();
    }
}
