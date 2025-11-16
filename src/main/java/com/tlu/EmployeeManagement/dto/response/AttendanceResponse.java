package com.tlu.EmployeeManagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Attendance record response")
public class AttendanceResponse {
    @Schema(description = "Attendance record ID", example = "1")
    Integer id;

    @Schema(description = "Employee ID", example = "123")
    Integer empId;

    @Schema(description = "Check-in timestamp", example = "16/11/2025 08:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime checkIn;

    @Schema(description = "Check-out timestamp", example = "16/11/2025 17:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime checkOut;

    @Schema(description = "Overtime hours worked", example = "2.5")
    BigDecimal overtimeHours;

    @Schema(description = "Record creation timestamp", example = "16/11/2025 08:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime createdAt;

    @Schema(description = "Record last update timestamp", example = "16/11/2025 17:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime updatedAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer empId;
        private LocalDateTime checkIn;
        private LocalDateTime checkOut;
        private BigDecimal overtimeHours;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

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

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public AttendanceResponse build() {
            AttendanceResponse response = new AttendanceResponse();
            response.id = this.id;
            response.empId = this.empId;
            response.checkIn = this.checkIn;
            response.checkOut = this.checkOut;
            response.overtimeHours = this.overtimeHours;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            return response;
        }
    }
}
