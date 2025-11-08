package com.tlu.EmployeeManagement.entity;

import com.tlu.EmployeeManagement.enums.UserRole;
import com.tlu.EmployeeManagement.enums.UserStatus;

import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbtractEntity {

    private String username;

    private String password;

    private String email;

    private UserRole role;

    private UserStatus status;
}