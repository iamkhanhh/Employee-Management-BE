package com.tlu.EmployeeManagement.dto.response;

import com.tlu.EmployeeManagement.enums.ContractType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Contract type distribution statistics")
public class ContractTypeStats {

    @Schema(description = "Contract type", example = "FULL_TIME")
    private ContractType contractType;

    @Schema(description = "Number of employees with this contract type", example = "120")
    private Long employeeCount;
}
