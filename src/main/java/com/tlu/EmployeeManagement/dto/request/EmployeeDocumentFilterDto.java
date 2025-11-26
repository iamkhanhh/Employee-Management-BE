package com.tlu.EmployeeManagement.dto.request;

import com.tlu.EmployeeManagement.enums.DocumentType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDocumentFilterDto {

    Integer page = 0;
    Integer pageSize = 10;
    Integer empId;
    DocumentType docType;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private Integer empId;
        private DocumentType docType;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder docType(DocumentType docType) {
            this.docType = docType;
            return this;
        }

        public EmployeeDocumentFilterDto build() {
            EmployeeDocumentFilterDto dto = new EmployeeDocumentFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.empId = this.empId;
            dto.docType = this.docType;
            return dto;
        }
    }
}
