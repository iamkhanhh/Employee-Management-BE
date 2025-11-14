package com.tlu.EmployeeManagement.entity;

import com.tlu.EmployeeManagement.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "employee_documents")
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeDocument extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", length = 100)
    private DocumentType docType;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private DocumentType docType;
        private String fileUrl;

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

        public EmployeeDocument build() {
            EmployeeDocument employeeDocument = new EmployeeDocument();
            employeeDocument.empId = this.empId;
            employeeDocument.docType = this.docType;
            employeeDocument.fileUrl = this.fileUrl;
            return employeeDocument;
        }
    }
}
