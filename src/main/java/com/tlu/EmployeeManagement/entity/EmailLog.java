package com.tlu.EmployeeManagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "email_logs")
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailLog extends AbtractEntity {

    @Column(length = 150)
    private String recipient;

    @Column(length = 150)
    private String subject;

    @Column(length = 1000)
    private String body;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String recipient;
        private String subject;
        private String body;

        public Builder recipient(String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public EmailLog build() {
            EmailLog emailLog = new EmailLog();
            emailLog.recipient = this.recipient;
            emailLog.subject = this.subject;
            emailLog.body = this.body;
            return emailLog;
        }
    }
}
