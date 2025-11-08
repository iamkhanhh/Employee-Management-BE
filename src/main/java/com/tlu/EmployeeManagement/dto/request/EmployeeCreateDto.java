package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.enums.Gender;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EmployeeCreateDto {

    @NotNull(message = "User ID is required")
    Integer userId;

    @NotNull(message = "Department ID is required")
    Integer deptId;

    @NotBlank(message = "Full name is required")
    String fullName;

    @NotNull(message = "Gender is required")
    Gender gender;

    @NotNull(message = "Date of birth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dob;

    @Pattern(regexp = "^[0-9]{10,20}$", message = "Phone number must be between 10 and 20 digits")
    String phoneNumber;

    String address;

    @NotNull(message = "Hire date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate hireDate;

    EmployeeStatus status;

    RoleInDepartment roleInDept;
}
