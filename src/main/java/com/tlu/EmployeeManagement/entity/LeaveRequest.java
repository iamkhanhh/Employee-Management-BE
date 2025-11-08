package com.tlu.EmployeeManagement.entity;

import java.time.LocalDate;

import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.enums.LeaveType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "leave_requests")
@Data
@EqualsAndHashCode(callSuper = true)
public class LeaveRequest extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", length = 50)
    private LeaveType leaveType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;
}
