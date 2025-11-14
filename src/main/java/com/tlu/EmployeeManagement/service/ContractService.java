package com.tlu.EmployeeManagement.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tlu.EmployeeManagement.dto.request.ContractCreateDto;
import com.tlu.EmployeeManagement.dto.request.ContractFilterDto;
import com.tlu.EmployeeManagement.dto.request.ContractUpdateDto;
import com.tlu.EmployeeManagement.dto.response.ContractResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.entity.Contract;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.enums.ContractStatus;
import com.tlu.EmployeeManagement.repository.ContractRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.specification.ContractSpecification;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContractService {
    ContractRepository contractRepository;
    EmployeeRepository employeeRepository;

    public PagedResponse<ContractResponse> getContracts(ContractFilterDto filterDto) {
        // Build specification for filtering
        Specification<Contract> spec = ContractSpecification.filterContracts(
            filterDto.getStatus(),
            filterDto.getContractType(),
            filterDto.getEmpId(),
            filterDto.getStartDate(),
            filterDto.getEndDate()
        );

        // Create pageable with sorting by createdAt descending
        Pageable pageable = PageRequest.of(
            filterDto.getPage(),
            filterDto.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Contract> contractPage = contractRepository.findAll(spec, pageable);

        List<ContractResponse> contractResponses = contractPage.getContent().stream()
            .map(this::toContractResponse)
            .collect(Collectors.toList());

        return PagedResponse.<ContractResponse>builder()
            .content(contractResponses)
            .currentPage(contractPage.getNumber())
            .pageSize(contractPage.getSize())
            .totalElements(contractPage.getTotalElements())
            .totalPages(contractPage.getTotalPages())
            .hasNext(contractPage.hasNext())
            .hasPrevious(contractPage.hasPrevious())
            .build();
    }

    public ContractResponse getContractById(Integer id) {
        Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        if (contract.getIsDeleted()) {
            throw new RuntimeException("Contract has been deleted");
        }

        return toContractResponse(contract);
    }

    public ContractResponse createContract(ContractCreateDto createDto) {
        // Validate employee exists
        employeeRepository.findById(createDto.getEmpId())
            .orElseThrow(() -> new RuntimeException("Employee not found with id: " + createDto.getEmpId()));

        Contract contract = Contract.builder()
            .empId(createDto.getEmpId())
            .contractType(createDto.getContractType())
            .startDate(createDto.getStartDate())
            .endDate(createDto.getEndDate())
            .fileUrl(createDto.getFileUrl())
            .status(createDto.getStatus() != null ? createDto.getStatus() : ContractStatus.ACTIVE)
            .build();

        contract.setIsDeleted(false);

        Contract savedContract = contractRepository.save(contract);
        return toContractResponse(savedContract);
    }

    public ContractResponse updateContract(Integer id, ContractUpdateDto updateDto) {
        Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        if (contract.getIsDeleted()) {
            throw new RuntimeException("Cannot update deleted contract");
        }

        if (updateDto.getContractType() != null) {
            contract.setContractType(updateDto.getContractType());
        }
        if (updateDto.getStartDate() != null) {
            contract.setStartDate(updateDto.getStartDate());
        }
        if (updateDto.getEndDate() != null) {
            contract.setEndDate(updateDto.getEndDate());
        }
        if (updateDto.getFileUrl() != null) {
            contract.setFileUrl(updateDto.getFileUrl());
        }
        if (updateDto.getStatus() != null) {
            contract.setStatus(updateDto.getStatus());
        }

        Contract updatedContract = contractRepository.save(contract);
        return toContractResponse(updatedContract);
    }

    public void deleteContract(Integer id) {
        Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Contract not found with id: " + id));

        // Soft delete
        contract.setIsDeleted(true);
        contractRepository.save(contract);
    }

    public ContractResponse toContractResponse(Contract contract) {
        // Get employee information
        String employeeName = null;
        if (contract.getEmpId() != null) {
            employeeName = employeeRepository.findById(contract.getEmpId())
                .map(Employee::getFullName)
                .orElse(null);
        }

        return ContractResponse.builder()
            .id(contract.getId())
            .empId(contract.getEmpId())
            .employeeName(employeeName)
            .contractType(contract.getContractType() != null ? contract.getContractType().name() : null)
            .startDate(contract.getStartDate())
            .endDate(contract.getEndDate())
            .fileUrl(contract.getFileUrl())
            .status(contract.getStatus() != null ? contract.getStatus().name() : null)
            .createdAt(contract.getCreatedAt())
            .build();
    }
}
