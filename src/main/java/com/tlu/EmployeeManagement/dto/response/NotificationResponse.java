package com.tlu.EmployeeManagement.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing notification details")
public class NotificationResponse {

    @Schema(
        description = "Unique identifier of the notification",
        example = "1"
    )
    Integer id;

    @Schema(
        description = "Title of the notification",
        example = "Department Meeting",
        maxLength = 150
    )
    String title;

    @Schema(
        description = "Content or body of the notification message",
        example = "Team meeting scheduled for 3 PM today in the main conference room.",
        maxLength = 500
    )
    String content;

    @Schema(
        description = "Name of the department this notification belongs to",
        example = "IT Department"
    )
    String departmentName;

    @Schema(
        description = "Full name of the user who created this notification (admin or department head)",
        example = "John Doe"
    )
    String createdByName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(
        description = "Date and time when the notification was created",
        example = "30/11/2025 14:30:45",
        type = "string",
        pattern = "dd/MM/yyyy HH:mm:ss"
    )
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Schema(
        description = "Date and time when the notification was last updated",
        example = "30/11/2025 16:20:15",
        type = "string",
        pattern = "dd/MM/yyyy HH:mm:ss"
    )
    LocalDateTime updatedAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String title;
        private String content;
        private String departmentName;
        private String createdByName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder createdByName(String createdByName) {
            this.createdByName = createdByName;
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

        public NotificationResponse build() {
            NotificationResponse response = new NotificationResponse();
            response.id = this.id;
            response.title = this.title;
            response.content = this.content;
            response.departmentName = this.departmentName;
            response.createdByName = this.createdByName;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            return response;
        }
    }
}
