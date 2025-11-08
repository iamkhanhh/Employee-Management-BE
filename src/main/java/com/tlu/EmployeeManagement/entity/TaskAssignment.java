package com.tlu.EmployeeManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "task_assignment")
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskAssignment extends AbtractEntity {

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;
}
