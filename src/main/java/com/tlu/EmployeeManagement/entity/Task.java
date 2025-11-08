package com.tlu.EmployeeManagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tlu.EmployeeManagement.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "tasks")
@Data
@EqualsAndHashCode(callSuper = true)
public class Task extends AbtractEntity {

    @Column(length = 255)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TaskStatus status = TaskStatus.PENDING;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "due_date")
    private LocalDate dueDate;
}
