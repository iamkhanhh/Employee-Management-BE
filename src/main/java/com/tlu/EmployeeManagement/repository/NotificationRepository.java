package com.tlu.EmployeeManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>, JpaSpecificationExecutor<Notification> {

    Optional<Notification> findById(Integer id);

    @Query("SELECT n FROM Notification n WHERE n.deptId = :deptId AND n.isDeleted = false ORDER BY n.createdAt DESC")
    Page<Notification> findByDeptIdAndIsDeletedFalse(@Param("deptId") Integer deptId, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.deptId = :deptId AND n.isDeleted = false ORDER BY n.createdAt DESC")
    List<Notification> findTopByDeptId(@Param("deptId") Integer deptId, Pageable pageable);

    @Query("SELECT n FROM Notification n WHERE n.isDeleted = false ORDER BY n.createdAt DESC")
    List<Notification> findTopNotifications(Pageable pageable);
}
