package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeResponse {
    Integer id;

    String fullName;

    String gender;

    String phoneNumber;

    String department;

    String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dob;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate hireDate;

    String roleInDept;

    String status;

    String username;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDateTime createdAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String fullName;
        private String gender;
        private String phoneNumber;
        private String department;
        private String address;
        private LocalDate dob;
        private LocalDate hireDate;
        private String roleInDept;
        private String status;
        private String username;
        private LocalDateTime createdAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder department(String department) {
            this.department = department;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder hireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public Builder roleInDept(String roleInDept) {
            this.roleInDept = roleInDept;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public EmployeeResponse build() {
            EmployeeResponse response = new EmployeeResponse();
            response.id = this.id;
            response.fullName = this.fullName;
            response.gender = this.gender;
            response.phoneNumber = this.phoneNumber;
            response.department = this.department;
            response.address = this.address;
            response.dob = this.dob;
            response.hireDate = this.hireDate;
            response.roleInDept = this.roleInDept;
            response.status = this.status;
            response.username = this.username;
            response.createdAt = this.createdAt;
            return response;
        }
    }
}
