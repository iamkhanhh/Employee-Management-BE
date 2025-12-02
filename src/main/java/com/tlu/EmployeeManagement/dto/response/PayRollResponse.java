package com.tlu.EmployeeManagement.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayRollResponse {
    Integer id;

    Integer empId;

    String empName;

    String empPosition;

    String empDepartment;

    Double allowance;

    Double bonus;

    Double deduction;

    Double netSalary;

    String status;

    String fileUrl;

}
