package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data transfer object for creating a notification")
public class NotificationCreateDto {

    @NotBlank(message = "Title is required")
    @Size(max = 150, message = "Title must not exceed 150 characters")
    @Schema(
        description = "Notification title",
        example = "Department Meeting",
        required = true,
        maxLength = 150
    )
    String title;

    @NotBlank(message = "Content is required")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    @Schema(
        description = "Notification content or message body",
        example = "Team meeting scheduled for 3 PM today in the main conference room. Please attend on time.",
        required = true,
        maxLength = 500
    )
    String content;

    @Schema(
        description = "ID of the department this notification belongs to. If null, the notification will be visible to all employees (admin only). Department heads must specify their department ID.",
        example = "1",
        nullable = true
    )
    Integer deptId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String content;
        private Integer deptId;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public NotificationCreateDto build() {
            NotificationCreateDto dto = new NotificationCreateDto();
            dto.title = this.title;
            dto.content = this.content;
            dto.deptId = this.deptId;
            return dto;
        }
    }
}
