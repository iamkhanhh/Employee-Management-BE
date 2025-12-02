package com.tlu.EmployeeManagement.dto.request;

import java.util.List;

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
public class EmployeeReviewSubmitDto {

    @NotNull(message = "Employee ID is required")
    Integer empId;

    @NotEmpty(message = "Scores cannot be empty")
    @Valid
    List<KpiScoreSubmitDto> scores;

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
