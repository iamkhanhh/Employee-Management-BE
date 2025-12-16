package com.tlu.EmployeeManagement.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Data Transfer Object for submitting an employee performance review")
public class EmployeeReviewSubmitDto {

    @Schema(description = "Employee ID being evaluated", example = "123", required = true)
    @NotNull(message = "Employee ID is required")
    Integer empId;

    @Schema(description = "List of scores for each KPI criteria", required = true)
    @NotEmpty(message = "Scores cannot be empty")
    @Valid
    List<KpiScoreSubmitDto> scores;

    @Schema(description = "Additional comments from the reviewer", example = "Outstanding performance this quarter")
    String comment;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer empId;
        private List<KpiScoreSubmitDto> scores;
        private String comment;

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder scores(List<KpiScoreSubmitDto> scores) {
            this.scores = scores;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public EmployeeReviewSubmitDto build() {
            EmployeeReviewSubmitDto dto = new EmployeeReviewSubmitDto();
            dto.empId = this.empId;
            dto.scores = this.scores;
            dto.comment = this.comment;
            return dto;
        }
    }
}
