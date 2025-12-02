package com.tlu.EmployeeManagement.dto.request;

import com.tlu.EmployeeManagement.enums.DocumentType;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDocumentCreateDto {

    Integer empId;

    @NotNull(message = "Document type is required")
    DocumentType docType;

    String fileUrl;

    String originalName;

    Long fileSize;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private DocumentType docType;
        private String fileUrl;
        private String originalName;
        private Long fileSize;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder docType(DocumentType docType) {
            this.docType = docType;
            return this;
        }

        public Builder fileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
            return this;
        }

        public Builder originalName(String originalName) {
            this.originalName = originalName;
            return this;
        }

        public Builder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public EmployeeDocumentCreateDto build() {
            EmployeeDocumentCreateDto dto = new EmployeeDocumentCreateDto();
            dto.empId = this.empId;
            dto.docType = this.docType;
            dto.fileUrl = this.fileUrl;
            dto.originalName = this.originalName;
            dto.fileSize = this.fileSize;
            return dto;
        }
    }
}
