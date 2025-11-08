package com.tlu.EmployeeManagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tlu.EmployeeManagement.entity.Task;
import com.tlu.EmployeeManagement.enums.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findById(Integer id);

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByCreatedBy(Integer createdBy);

    @Query("SELECT t FROM Task t WHERE t.isDeleted = false")
    List<Task> findAllActive();

    @Query("SELECT t FROM Task t WHERE t.dueDate < :date AND t.status = :status")
    List<Task> findOverdueTasks(@Param("date") LocalDate date, @Param("status") TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByDueDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    void deleteById(Integer id);
}
