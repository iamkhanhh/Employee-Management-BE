package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    Optional<Notification> findById(Integer id);

    List<Notification> findByCreatedBy(Integer createdBy);

    @Query("SELECT n FROM Notification n WHERE n.isDeleted = false ORDER BY n.createdAt DESC")
    List<Notification> findAllActive();

    @Query("SELECT n FROM Notification n WHERE n.createdBy = :userId AND n.isDeleted = false ORDER BY n.createdAt DESC")
    List<Notification> findActiveByCreatedBy(@Param("userId") Integer userId);

    void deleteById(Integer id);
}
