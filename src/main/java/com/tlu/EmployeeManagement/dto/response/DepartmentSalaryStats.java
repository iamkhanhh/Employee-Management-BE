package com.tlu.EmployeeManagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Total salary statistics by department")
public class DepartmentSalaryStats {

    @Schema(description = "Department ID", example = "1")
    private Integer deptId;

    @Schema(description = "Department name", example = "Engineering")
    private String deptName;

    @Schema(description = "Total basic salary of all employees in this department", example = "125000.00")
    private Double totalSalary;
}
