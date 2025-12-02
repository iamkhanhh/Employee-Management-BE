package com.tlu.EmployeeManagement.dto.request;

import lombok.*; 
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DepartmentPayrollDto {
     Integer deptId;
}
