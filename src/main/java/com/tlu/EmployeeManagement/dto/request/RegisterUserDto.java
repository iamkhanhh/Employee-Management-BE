package com.tlu.EmployeeManagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUserDto {
    @Size(min = 3, message = "Username must be at least 3 characters")
    String username;

    @Size(min = 8, message = "Password must be at least 8 characters")
    String password;

    @Email()
    String email;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String username;
        private String password;
        private String email;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }


        public RegisterUserDto build() {
            RegisterUserDto dto = new RegisterUserDto();
            dto.username = this.username;
            dto.password = this.password;
            dto.email = this.email;
            return dto;
        }
    }

}
