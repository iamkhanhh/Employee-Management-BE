package com.tlu.EmployeeManagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginDto {
  @Email()
  String email;

  @Size(min = 8, message = "Password must be at least 8 characters")
  String password;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String email;
    private String password;

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public LoginDto build() {
      LoginDto dto = new LoginDto();
      dto.email = this.email;
      dto.password = this.password;
      return dto;
    }
  }
}
