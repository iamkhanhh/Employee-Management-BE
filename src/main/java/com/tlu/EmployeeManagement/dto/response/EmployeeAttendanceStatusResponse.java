package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Employee attendance status for a specific day")
public class EmployeeAttendanceStatusResponse {
    @Schema(description = "Employee ID", example = "1")
    Integer employeeId;

    @Schema(description = "Employee full name", example = "John Doe")
    String fullName;

    @Schema(description = "Attendance status", example = "PRESENT", allowableValues = {"PRESENT", "ABSENT", "LATE"})
    String status;

    @Schema(description = "Check-in timestamp (null if absent)", example = "16/11/2025 08:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime checkInTime;

    @Schema(description = "Check-out timestamp (null if not checked out)", example = "16/11/2025 17:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime checkOutTime;

    @Schema(description = "Whether the employee was late", example = "true")
    Boolean isLate;

    @Schema(description = "Number of minutes late (null if not late)", example = "15 minutes")
    String lateMinutes;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer employeeId;
        private String fullName;
        private String status;
        private LocalDateTime checkInTime;
        private LocalDateTime checkOutTime;
        private Boolean isLate;
        private String lateMinutes;

        public Builder employeeId(Integer employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder checkInTime(LocalDateTime checkInTime) {
            this.checkInTime = checkInTime;
            return this;
        }

        public Builder checkOutTime(LocalDateTime checkOutTime) {
            this.checkOutTime = checkOutTime;
            return this;
        }

        public Builder isLate(Boolean isLate) {
            this.isLate = isLate;
            return this;
        }

        public Builder lateMinutes(String lateMinutes) {
            this.lateMinutes = lateMinutes;
            return this;
        }

        public EmployeeAttendanceStatusResponse build() {
            EmployeeAttendanceStatusResponse response = new EmployeeAttendanceStatusResponse();
            response.employeeId = this.employeeId;
            response.fullName = this.fullName;
            response.status = this.status;
            response.checkInTime = this.checkInTime;
            response.checkOutTime = this.checkOutTime;
            response.isLate = this.isLate;
            response.lateMinutes = this.lateMinutes;
            return response;
        }
    }
}
