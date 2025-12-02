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
@Schema(description = "Data transfer object for email verification")
public class VerifiDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Schema(
        description = "Email address to verify",
        example = "john.doe@example.com",
        required = true
    )
    String email;

    @NotBlank(message = "Verification code is required")
    @Schema(
        description = "Verification code sent to the email",
        example = "123456",
        required = true
    )
    String verificationCode;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String verificationCode;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder verificationCode(String verificationCode) {
            this.verificationCode = verificationCode;
            return this;
        }

        public VerifiDto build() {
            VerifiDto dto = new VerifiDto();
            dto.email = this.email;
            dto.verificationCode = this.verificationCode;
            return dto;
        }
    }
}
