package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KpiPeriodFilterDto {

    Integer page = 0;

    Integer pageSize = 10;

    String periodName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    LocalDate endDate;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private String periodName;
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder periodName(String periodName) {
            this.periodName = periodName;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public KpiPeriodFilterDto build() {
            KpiPeriodFilterDto dto = new KpiPeriodFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.periodName = this.periodName;
            dto.startDate = this.startDate;
            dto.endDate = this.endDate;
            return dto;
        }
    }
}
