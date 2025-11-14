package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.tlu.EmployeeManagement.enums.UserRole;
import com.tlu.EmployeeManagement.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String currentPassword;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String gender;

    private String country;

    private String fullName;

    private String profilePicImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dob;

    private UserRole role;

    private UserStatus status;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String currentPassword;
        private String password;
        private String gender;
        private String country;
        private String fullName;
        private String profilePicImage;
        private LocalDate dob;
        private UserRole role;
        private UserStatus status;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder profilePicImage(String profilePicImage) {
            this.profilePicImage = profilePicImage;
            return this;
        }

        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder role(UserRole role) {
            this.role = role;
            return this;
        }

        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public UserUpdateDto build() {
            UserUpdateDto dto = new UserUpdateDto();
            dto.username = this.username;
            dto.currentPassword = this.currentPassword;
            dto.password = this.password;
            dto.gender = this.gender;
            dto.country = this.country;
            dto.fullName = this.fullName;
            dto.profilePicImage = this.profilePicImage;
            dto.dob = this.dob;
            dto.role = this.role;
            dto.status = this.status;
            return dto;
        }
    }

}
