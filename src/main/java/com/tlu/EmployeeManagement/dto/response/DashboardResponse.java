package com.tlu.EmployeeManagement.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dashboard statistics response containing all dashboard metrics")
public class DashboardResponse {

    @Schema(description = "Overview statistics including total employees, departments, new hires, and turnover")
    private OverviewStats overviewStats;

    @Schema(description = "Personnel distribution across departments")
    private List<DepartmentPersonnelStats> personnelByDepartment;

    @Schema(description = "Contract type distribution statistics")
    private List<ContractTypeStats> contractTypeStats;

    @Schema(description = "Total salary by department")
    private List<DepartmentSalaryStats> salaryByDepartment;

    @Schema(description = "Employee count trend over the last 12 months")
    private List<MonthlyEmployeeCount> employeeCountOverTime;
}
