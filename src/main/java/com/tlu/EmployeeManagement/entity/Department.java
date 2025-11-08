package com.tlu.EmployeeManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "departments")
@Data
@EqualsAndHashCode(callSuper = true)
public class Department extends AbtractEntity {

    @Column(name = "dept_name", length = 100)
    private String deptName;
}
