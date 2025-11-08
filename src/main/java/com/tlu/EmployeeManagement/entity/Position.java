package com.tlu.EmployeeManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "positions")
@Data
@EqualsAndHashCode(callSuper = true)
public class Position extends AbtractEntity {

    @Column(name = "position_name", length = 100)
    private String positionName;

    @Column(length = 255)
    private String description;
}
