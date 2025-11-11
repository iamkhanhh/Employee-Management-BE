package com.tlu.EmployeeManagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class changePasswordDto {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String confirmPassword;

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
