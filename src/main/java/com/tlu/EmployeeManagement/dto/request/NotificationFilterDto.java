package com.tlu.EmployeeManagement.dto.request;

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
@Schema(description = "Filter criteria for querying notifications with pagination")
public class NotificationFilterDto {

    @Schema(
        description = "Page number (zero-based)",
        example = "0",
        defaultValue = "0"
    )
    Integer page = 0;

    @Schema(
        description = "Number of items per page",
        example = "10",
        defaultValue = "10"
    )
    Integer pageSize = 10;

    @Schema(
        description = "Filter notifications by department ID. Admin can view all departments, regular users can only view their own department",
        example = "1"
    )
    Integer deptId;

    @Schema(
        description = "Search keyword to filter notifications by title or content (case-insensitive)",
        example = "meeting"
    )
    String search;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer page = 0;
        private Integer pageSize = 10;
        private Integer deptId;
        private String search;

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public Builder search(String search) {
            this.search = search;
            return this;
        }

        public NotificationFilterDto build() {
            NotificationFilterDto dto = new NotificationFilterDto();
            dto.page = this.page;
            dto.pageSize = this.pageSize;
            dto.deptId = this.deptId;
            dto.search = this.search;
            return dto;
        }
    }
}
