package com.tlu.EmployeeManagement.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tlu.EmployeeManagement.dto.response.ContractTypeStats;
import com.tlu.EmployeeManagement.dto.response.DashboardResponse;
import com.tlu.EmployeeManagement.dto.response.DepartmentPersonnelStats;
import com.tlu.EmployeeManagement.dto.response.DepartmentSalaryStats;
import com.tlu.EmployeeManagement.dto.response.MonthlyEmployeeCount;
import com.tlu.EmployeeManagement.dto.response.OverviewStats;
import com.tlu.EmployeeManagement.entity.Contract;
import com.tlu.EmployeeManagement.entity.Department;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.enums.ContractStatus;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.repository.ContractRepository;
import com.tlu.EmployeeManagement.repository.DepartmentRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonService {

    EmployeeRepository employeeRepository;
    DepartmentRepository departmentRepository;
    ContractRepository contractRepository;

    public DashboardResponse getDashboardStats() {
        return DashboardResponse.builder()
                .overviewStats(getOverviewStats())
                .personnelByDepartment(getPersonnelByDepartment())
                .contractTypeStats(getContractTypeStats())
                .salaryByDepartment(getSalaryByDepartment())
                .employeeCountOverTime(getEmployeeCountOverTime())
                .build();
    }

    private OverviewStats getOverviewStats() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());

        Long totalEmployees = employeeRepository.countByIsDeletedAndStatus(false, EmployeeStatus.ACTIVE);

        Long totalDepartments = departmentRepository.countByIsDeleted(false);

        Long newHiresThisMonth = employeeRepository.countByIsDeletedAndHireDateBetween(
                false, startOfMonth, endOfMonth);

        Long staffTurnoverThisMonth = employeeRepository.countByIsDeletedAndStatusAndUpdatedAtBetween(
                false, EmployeeStatus.TERMINATED, startOfMonth.atStartOfDay(), endOfMonth.atTime(23, 59, 59));

        return OverviewStats.builder()
                .totalEmployees(totalEmployees)
                .totalDepartments(totalDepartments)
                .newHiresThisMonth(newHiresThisMonth)
                .staffTurnoverThisMonth(staffTurnoverThisMonth)
                .build();
    }

    private List<DepartmentPersonnelStats> getPersonnelByDepartment() {
        List<Employee> activeEmployees = employeeRepository.findByIsDeletedAndStatus(false, EmployeeStatus.ACTIVE);
        List<Department> departments = departmentRepository.findByIsDeleted(false);

        Map<Integer, Long> employeeCountByDept = activeEmployees.stream()
                .filter(emp -> emp.getDeptId() != null)
                .collect(Collectors.groupingBy(
                        Employee::getDeptId,
                        Collectors.counting()));

        return departments.stream()
                .map(dept -> DepartmentPersonnelStats.builder()
                        .deptId(dept.getId())
                        .deptName(dept.getDeptName())
                        .employeeCount(employeeCountByDept.getOrDefault(dept.getId(), 0L))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ContractTypeStats> getContractTypeStats() {
        List<Contract> activeContracts = contractRepository.findByIsDeletedAndStatus(false, ContractStatus.ACTIVE);

        List<Employee> activeEmployees = employeeRepository.findByIsDeletedAndStatus(false, EmployeeStatus.ACTIVE);
        Map<Integer, Employee> employeeMap = activeEmployees.stream()
                .collect(Collectors.toMap(Employee::getId, emp -> emp));

        Map<com.tlu.EmployeeManagement.enums.ContractType, Long> contractTypeCount = activeContracts.stream()
                .filter(contract -> employeeMap.containsKey(contract.getEmpId()))
                .collect(Collectors.groupingBy(
                        Contract::getContractType,
                        Collectors.counting()));

        return contractTypeCount.entrySet().stream()
                .map(entry -> ContractTypeStats.builder()
                        .contractType(entry.getKey())
                        .employeeCount(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private List<DepartmentSalaryStats> getSalaryByDepartment() {
        List<Employee> activeEmployees = employeeRepository.findByIsDeletedAndStatus(false, EmployeeStatus.ACTIVE);
        List<Department> departments = departmentRepository.findByIsDeleted(false);

        Map<Integer, Double> salaryByDept = activeEmployees.stream()
                .filter(emp -> emp.getDeptId() != null && emp.getBasicSalary() != null)
                .collect(Collectors.groupingBy(
                        Employee::getDeptId,
                        Collectors.summingDouble(emp -> emp.getBasicSalary().doubleValue())));

        return departments.stream()
                .map(dept -> DepartmentSalaryStats.builder()
                        .deptId(dept.getId())
                        .deptName(dept.getDeptName())
                        .totalSalary(salaryByDept.getOrDefault(dept.getId(), 0.0))
                        .build())
                .collect(Collectors.toList());
    }

    private List<MonthlyEmployeeCount> getEmployeeCountOverTime() {
        List<MonthlyEmployeeCount> monthlyData = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        List<Employee> allEmployees = employeeRepository.findByIsDeleted(false);

        for (int i = 11; i >= 0; i--) {
            YearMonth month = currentMonth.minusMonths(i);
            LocalDate endOfMonth = month.atEndOfMonth();

            long count = allEmployees.stream()
                    .filter(emp -> emp.getHireDate() != null && !emp.getHireDate().isAfter(endOfMonth))
                    .filter(emp -> {
                        if (emp.getStatus() == EmployeeStatus.ACTIVE) {
                            return true;
                        }
                        if (emp.getStatus() == EmployeeStatus.TERMINATED && emp.getUpdatedAt() != null) {
                            return emp.getUpdatedAt().toLocalDate().isAfter(endOfMonth);
                        }
                        return false;
                    })
                    .count();

            monthlyData.add(MonthlyEmployeeCount.builder()
                    .month(month.format(formatter))
                    .employeeCount(count)
                    .build());
        }

        return monthlyData;
    }
}
