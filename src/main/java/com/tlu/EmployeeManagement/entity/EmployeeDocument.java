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
}
