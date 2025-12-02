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
@Schema(description = "Personnel statistics by department")
public class DepartmentPersonnelStats {

    @Schema(description = "Department ID", example = "1")
    private Integer deptId;

    @Schema(description = "Department name", example = "Engineering")
    private String deptName;

    @Schema(description = "Number of employees in this department", example = "25")
    private Long employeeCount;
}
