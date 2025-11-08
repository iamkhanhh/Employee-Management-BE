package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.enums.Gender;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateDto {

    Integer deptId;

    String fullName;

    Gender gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dob;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be between 10 and 20 digits")
    String phoneNumber;

    String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate hireDate;

    EmployeeStatus status;

    RoleInDepartment roleInDept;
}
