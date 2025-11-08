package com.tlu.EmployeeManagement.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.EmailLog;

@Repository
public interface EmailLogRepository extends JpaRepository<EmailLog, Integer> {

    Optional<EmailLog> findById(Integer id);

    List<EmailLog> findByRecipient(String recipient);

    @Query("SELECT el FROM EmailLog el WHERE el.createdAt BETWEEN :startDate AND :endDate ORDER BY el.createdAt DESC")
    List<EmailLog> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT el FROM EmailLog el WHERE el.recipient = :recipient AND el.createdAt BETWEEN :startDate AND :endDate")
    List<EmailLog> findByRecipientAndDateRange(
        @Param("recipient") String recipient,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT el FROM EmailLog el WHERE el.isDeleted = false ORDER BY el.createdAt DESC")
    List<EmailLog> findAllActive();

    void deleteById(Integer id);
}
