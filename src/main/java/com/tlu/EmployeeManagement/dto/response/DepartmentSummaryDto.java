package com.tlu.EmployeeManagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSummaryDto {
    private Integer id;
    private String deptName;
    private String managerName; 
    private Long employeeCount;
}
