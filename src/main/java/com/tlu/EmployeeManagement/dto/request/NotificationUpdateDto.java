package com.tlu.EmployeeManagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Data transfer object for updating a notification. All fields are optional for partial updates.")
public class NotificationUpdateDto {

    @Size(max = 150, message = "Title must not exceed 150 characters")
    @Schema(
        description = "Updated notification title",
        example = "Updated Meeting Time",
        maxLength = 150
    )
    String title;

    @Size(max = 500, message = "Content must not exceed 500 characters")
    @Schema(
        description = "Updated notification content or message body",
        example = "Meeting has been rescheduled to 4 PM today due to unforeseen circumstances.",
        maxLength = 500
    )
    String content;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String content;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public NotificationUpdateDto build() {
            NotificationUpdateDto dto = new NotificationUpdateDto();
            dto.title = this.title;
            dto.content = this.content;
            return dto;
        }
    }
}
