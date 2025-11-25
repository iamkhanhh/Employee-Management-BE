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

    @Column(name = "assigned_date")
    private LocalDateTime assignedDate;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer taskId;
        private Integer empId;
        private LocalDateTime completedDate;
    private LocalDateTime assignedDate;

        public Builder taskId(Integer taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder completedDate(LocalDateTime completedDate) {
            this.completedDate = completedDate;
            return this;
        }

        public Builder assignedDate(LocalDateTime assignedDate) {
            this.assignedDate = assignedDate;
            return this;
        }

        public TaskAssignment build() {
            TaskAssignment taskAssignment = new TaskAssignment();
            taskAssignment.taskId = this.taskId;
            taskAssignment.empId = this.empId;
            taskAssignment.completedDate = this.completedDate;
            taskAssignment.assignedDate = this.assignedDate;
            return taskAssignment;
        }
    }
}
