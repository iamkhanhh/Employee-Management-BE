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

    @Column(name = "reject_reason", length = 255)
    private String rejectReason; 

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "approved_date")
    private LocalDate approvedDate;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private LeaveType leaveType;
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;
        private LeaveStatus status;
        private Integer approvedBy;
        private LocalDate approvedDate;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder leaveType(LeaveType leaveType) {
            this.leaveType = leaveType;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder status(LeaveStatus status) {
            this.status = status;
            return this;
        }

        public Builder approvedBy(Integer approvedBy) {
            this.approvedBy = approvedBy;
            return this;
        }

        public Builder approvedDate(LocalDate approvedDate) {
            this.approvedDate = approvedDate;
            return this;
        }

        public LeaveRequest build() {
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.empId = this.empId;
            leaveRequest.leaveType = this.leaveType;
            leaveRequest.startDate = this.startDate;
            leaveRequest.endDate = this.endDate;
            leaveRequest.reason = this.reason;
            leaveRequest.status = this.status != null ? this.status : LeaveStatus.PENDING;
            leaveRequest.approvedBy = this.approvedBy;
            leaveRequest.approvedDate = this.approvedDate;
            return leaveRequest;
        }
    }
}
