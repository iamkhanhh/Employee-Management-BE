package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data transfer object for new user registration")
public class RegisterUserDto {

    @NotBlank(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    @Schema(
        description = "Unique username for the new user account",
        example = "johndoe",
        required = true,
        minLength = 3
    )
    String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(
        description = "Password for the new account (minimum 8 characters)",
        example = "securePassword123",
        required = true,
        minLength = 8
    )
    String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(
        description = "Valid email address for the new user",
        example = "john.doe@example.com",
        required = true
    )
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
