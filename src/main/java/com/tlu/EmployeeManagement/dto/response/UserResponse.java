package com.tlu.EmployeeManagement.dto.response;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.LocalDate;

import com.tlu.EmployeeManagement.enums.UserRole;
import com.tlu.EmployeeManagement.enums.UserStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
  Integer id;

  String username;

  String email;

  String gender;

  String country;

  String profilePicImage;

  String fullName;

  UserRole role;

  UserStatus status;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDate dob;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private Integer id;
    private String username;
    private String email;
    private String gender;
    private String country;
    private String profilePicImage;
    private String fullName;
    private UserRole role;
    private UserStatus status;
    private LocalDate dob;
    private LocalDateTime createdAt;

    public Builder id(Integer id) {
      this.id = id;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder gender(String gender) {
      this.gender = gender;
      return this;
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    public Builder profilePicImage(String profilePicImage) {
      this.profilePicImage = profilePicImage;
      return this;
    }

    public Builder fullName(String fullName) {
      this.fullName = fullName;
      return this;
    }

    public Builder role(UserRole role) {
      this.role = role;
      return this;
    }

    public Builder status(UserStatus status) {
      this.status = status;
      return this;
    }

    public Builder dob(LocalDate dob) {
      this.dob = dob;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public UserResponse build() {
      UserResponse response = new UserResponse();
      response.id = this.id;
      response.username = this.username;
      response.email = this.email;
      response.gender = this.gender;
      response.country = this.country;
      response.profilePicImage = this.profilePicImage;
      response.fullName = this.fullName;
      response.role = this.role;
      response.status = this.status;
      response.dob = this.dob;
      response.createdAt = this.createdAt;
      return response;
    }
  }
}
