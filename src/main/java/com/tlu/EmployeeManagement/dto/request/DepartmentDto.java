package com.tlu.EmployeeManagement.dto.request;

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
public class DepartmentDto {
    @NotBlank(message = "Department name is required")
    String deptName;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String deptName;

        public Builder deptName(String deptName) {
            this.deptName = deptName;
            return this;
        }

        public DepartmentDto build() {
            DepartmentDto dto = new DepartmentDto();
            dto.deptName = this.deptName;
            return dto;
        }
    }
}
