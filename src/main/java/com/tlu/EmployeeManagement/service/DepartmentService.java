package com.tlu.EmployeeManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tlu.EmployeeManagement.dto.request.DepartmentDto;
import com.tlu.EmployeeManagement.dto.response.DepartmentResponse;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Service
@RequiredArgsConstructor
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DepartmentService {

    DepartmentRepository departmentRepository;

    public DepartmentResponse createDepartment(DepartmentDto createDto) {

        departmentRepository.findByDeptName(createDto.getDeptName())
            .ifPresent(d -> { throw new RuntimeException("Department name already exists"); });

        Department department = new Department();
        department.setDeptName(createDto.getDeptName());
        department.setIsDeleted(false);

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

        department.setDeptName(updateDto.getDeptName());

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

    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findByIsDeletedFalse().stream()
                .map(this::toDepartmentResponse)
                .collect(Collectors.toList());
    }

    private DepartmentResponse toDepartmentResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .deptName(department.getDeptName())
                .createdAt(department.getCreatedAt())
                .build();
    }
}
