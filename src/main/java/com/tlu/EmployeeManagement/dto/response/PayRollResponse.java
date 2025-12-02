package com.tlu.EmployeeManagement.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;



@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Payroll response containing employee payroll details")
public class PayRollResponse {
    @Schema(description = "Payroll ID", example = "1")
    Integer id;

    @Schema(description = "Employee ID", example = "1")
    Integer empId;

    @Schema(description = "Employee name", example = "John Doe")
    String empName;

    @Schema(description = "Employee position", example = "Software Engineer")
    String empPosition;

    @Schema(description = "Employee department", example = "IT Department")
    String empDepartment;

    @Schema(description = "Allowance amount", example = "500000.0")
    Double allowance;

    @Schema(description = "Bonus amount", example = "1000000.0")
    Double bonus;

    @Schema(description = "Deduction amount", example = "200000.0")
    Double deduction;

    @Schema(description = "Net salary after allowances, bonuses, and deductions", example = "15300000.0")
    Double netSalary;

    @Schema(description = "Payroll status", example = "PAID")
    String status;

    @Schema(description = "URL to payroll file/document", example = "https://example.com/payrolls/file.pdf")
    String fileUrl;

}
