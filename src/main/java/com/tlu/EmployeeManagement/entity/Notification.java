package com.tlu.EmployeeManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "notifications")
@Data
@EqualsAndHashCode(callSuper = true)
public class Notification extends AbtractEntity {

    @Column(length = 150)
    private String title;

    @Column(length = 500)
    private String content;

    @Column(name = "created_by")
    private Integer createdBy;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String content;
        private Integer createdBy;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder createdBy(Integer createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification();
            notification.title = this.title;
            notification.content = this.content;
            notification.createdBy = this.createdBy;
            return notification;
        }
    }
}
