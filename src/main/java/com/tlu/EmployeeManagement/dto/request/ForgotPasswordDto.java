package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data transfer object for initiating password reset process")
public class ForgotPasswordDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(
        description = "Email address of the account to reset password for",
        example = "john.doe@example.com",
        required = true
    )
    String email;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public ForgotPasswordDto build() {
            ForgotPasswordDto dto = new ForgotPasswordDto();
            dto.email = this.email;
            return dto;
        }
    }

}
