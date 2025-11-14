package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.UserStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterDto {

    Integer page = 0;

    Integer pageSize = 10;

    UserStatus status;

    String country;

    Integer deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate dob;

    String search;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private UserStatus status;
        private String country;
        private Integer deptId;
        private LocalDate dob;
        private String search;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public Builder dob(LocalDate dob) {
            this.dob = dob;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public UserFilterDto build() {
            UserFilterDto dto = new UserFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.status = this.status;
            dto.country = this.country;
            dto.deptId = this.deptId;
            dto.dob = this.dob;
            dto.search = this.search;
            return dto;
        }
    }

}
