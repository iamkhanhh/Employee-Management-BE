package com.tlu.EmployeeManagement.dto.request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordDto {
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
