package com.tlu.EmployeeManagement.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifiDto {
    private String email;
    private String verificationCode;

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
