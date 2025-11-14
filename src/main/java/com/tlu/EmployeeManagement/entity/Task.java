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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String description;
        private TaskStatus status;
        private Integer createdBy;
        private LocalDate dueDate;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public Builder createdBy(Integer createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.title = this.title;
            task.description = this.description;
            task.status = this.status != null ? this.status : TaskStatus.PENDING;
            task.createdBy = this.createdBy;
            task.dueDate = this.dueDate;
            return task;
        }
    }
}
