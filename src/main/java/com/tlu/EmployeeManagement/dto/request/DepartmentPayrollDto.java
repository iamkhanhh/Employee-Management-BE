package com.tlu.EmployeeManagement.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request body for creating payroll for all employees in a department")
public class DepartmentPayrollDto {
     @Schema(description = "Department ID", example = "1", required = true)
     Integer deptId;
}
