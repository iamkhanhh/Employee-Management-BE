package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeFilterDto {

    Integer page = 0;

    Integer pageSize = 10;

    EmployeeStatus status;

    Integer deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate hireDate;

    String search; // For searching in employee name

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private EmployeeStatus status;
        private Integer deptId;
        private LocalDate hireDate;
        private String search;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder status(EmployeeStatus status) {
            this.status = status;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public Builder hireDate(LocalDate hireDate) {
            this.hireDate = hireDate;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public EmployeeFilterDto build() {
            EmployeeFilterDto dto = new EmployeeFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.status = this.status;
            dto.deptId = this.deptId;
            dto.hireDate = this.hireDate;
            dto.search = this.search;
            return dto;
        }
    }
}
