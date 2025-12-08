package com.tlu.EmployeeManagement.dto.request;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.media.Schema;



@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request body for creating a single employee payroll")
public class PayRollUpdateDto {
    @Schema(description = "Allowance amount (tro cap)", example = "500000.00")
    BigDecimal allowance;

    @Schema(description = "Bonus amount (thuong)", example = "1000000.00")
    BigDecimal bonus;

    @Schema(description = "Deduction amount (tru)", example = "200000.00")
    BigDecimal deduction;

}
