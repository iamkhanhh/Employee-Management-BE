package com.tlu.EmployeeManagement.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tlu.EmployeeManagement.enums.ContractStatus;
import com.tlu.EmployeeManagement.enums.ContractType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContractUpdateDto {

    ContractType contractType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDate endDate;

    String fileUrl;

    ContractStatus status;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ContractType contractType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String fileUrl;
        private ContractStatus status;

        public Builder contractType(ContractType contractType) {
            this.contractType = contractType;
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

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder status(ContractStatus status) {
            this.status = status;
            return this;
        }

        public ContractUpdateDto build() {
            ContractUpdateDto dto = new ContractUpdateDto();
            dto.contractType = this.contractType;
            dto.startDate = this.startDate;
            dto.endDate = this.endDate;
            dto.fileUrl = this.fileUrl;
            dto.status = this.status;
            return dto;
        }
    }
}
