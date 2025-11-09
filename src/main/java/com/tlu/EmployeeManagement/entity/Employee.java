package com.tlu.EmployeeManagement.entity;

import java.time.LocalDate;

import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.enums.Gender;
import com.tlu.EmployeeManagement.enums.RoleInDepartment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employees")
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends AbtractEntity {

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "dept_id")
    private Integer deptId;

    @Column(name = "full_name", length = 150)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "basic_salary", precision = 12, scale = 2)
    private BigDecimal basicSalary;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_in_dept", length = 20)
    private RoleInDepartment roleInDept = RoleInDepartment.STAFF;
}
