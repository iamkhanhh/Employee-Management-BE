package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for changing user password")
public class changePasswordDto {

    @NotBlank(message = "Current password is required")
    @Schema(
        description = "User's current password for verification",
        example = "oldPassword123",
        required = true
    )
    String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(
        description = "New password (minimum 8 characters)",
        example = "newSecurePassword456",
        required = true,
        minLength = 8
    )
    String newPassword;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Schema(
        description = "Confirmation of new password (must match newPassword)",
        example = "newSecurePassword456",
        required = true,
        minLength = 8
    )
    String confirmPassword;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String currentPassword;
        private String newPassword;
        private String confirmPassword;

        public Builder currentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public Builder confirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public changePasswordDto build() {
            changePasswordDto dto = new changePasswordDto();
            dto.currentPassword = this.currentPassword;
            dto.newPassword = this.newPassword;
            dto.confirmPassword = this.confirmPassword;
            return dto;
        }
    }
}
