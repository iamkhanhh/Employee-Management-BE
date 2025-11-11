package com.tlu.EmployeeManagement.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordDto {
    String email;

    String newPassword;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String email;
        private String newPassword;

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder newPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public ForgotPasswordDto build() {
            ForgotPasswordDto dto = new ForgotPasswordDto();
            dto.email = this.email;
            dto.newPassword = this.newPassword;
            return dto;
        }
    }

}
