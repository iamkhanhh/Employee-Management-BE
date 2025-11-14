package com.tlu.EmployeeManagement.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "attendance")
@Data
@EqualsAndHashCode(callSuper = true)
public class Attendance extends AbtractEntity {

    @Column(name = "emp_id")
    private Integer empId;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    private LocalDateTime checkOut;

    @Column(name = "overtime_hours", precision = 5, scale = 2)
    private BigDecimal overtimeHours;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private LocalDateTime checkIn;
        private LocalDateTime checkOut;
        private BigDecimal overtimeHours;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder checkIn(LocalDateTime checkIn) {
            this.checkIn = checkIn;
            return this;
        }

        public Builder checkOut(LocalDateTime checkOut) {
            this.checkOut = checkOut;
            return this;
        }

        public Builder overtimeHours(BigDecimal overtimeHours) {
            this.overtimeHours = overtimeHours;
            return this;
        }

        public Attendance build() {
            Attendance attendance = new Attendance();
            attendance.empId = this.empId;
            attendance.checkIn = this.checkIn;
            attendance.checkOut = this.checkOut;
            attendance.overtimeHours = this.overtimeHours;
            return attendance;
        }
    }
}
