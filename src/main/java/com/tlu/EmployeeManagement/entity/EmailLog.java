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
}
