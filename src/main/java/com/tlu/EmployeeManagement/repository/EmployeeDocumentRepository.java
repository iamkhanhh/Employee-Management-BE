package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.EmployeeDocument;
import com.tlu.EmployeeManagement.enums.DocumentType;

@Repository
public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Integer> {

    Optional<EmployeeDocument> findById(Integer id);

    List<EmployeeDocument> findByEmpId(Integer empId);

    @Query("SELECT ed FROM EmployeeDocument ed WHERE ed.empId = :empId AND ed.isDeleted = false")
    List<EmployeeDocument> findActiveDocumentsByEmpId(@Param("empId") Integer empId);

    List<EmployeeDocument> findByEmpIdAndDocType(Integer empId, DocumentType docType);

    void deleteById(Integer id);
}
