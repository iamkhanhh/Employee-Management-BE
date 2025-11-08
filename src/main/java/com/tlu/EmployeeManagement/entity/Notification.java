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
}
