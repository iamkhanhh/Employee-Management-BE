package com.tlu.EmployeeManagement.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeReviewRecordDto {
    Integer id;

    Integer empId;

    String empName;

    Integer deptId;

    String deptName;

    List<EmployeeReviewScoreDto> scores;

    BigDecimal averageScore;

    String finalRating;

    String comment;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer empId;
        private String empName;
        private Integer deptId;
        private String deptName;
        private List<EmployeeReviewScoreDto> scores;
        private BigDecimal averageScore;
        private String finalRating;
        private String comment;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder empId(Integer empId) {
            this.empId = empId;
            return this;
        }

        public Builder empName(String empName) {
            this.empName = empName;
            return this;
        }

        public Builder deptId(Integer deptId) {
            this.deptId = deptId;
            return this;
        }

        public Builder deptName(String deptName) {
            this.deptName = deptName;
            return this;
        }

        public Builder scores(List<EmployeeReviewScoreDto> scores) {
            this.scores = scores;
            return this;
        }

        public Builder averageScore(BigDecimal averageScore) {
            this.averageScore = averageScore;
            return this;
        }

        public Builder finalRating(String finalRating) {
            this.finalRating = finalRating;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public EmployeeReviewRecordDto build() {
            EmployeeReviewRecordDto dto = new EmployeeReviewRecordDto();
            dto.id = this.id;
            dto.empId = this.empId;
            dto.empName = this.empName;
            dto.deptId = this.deptId;
            dto.deptName = this.deptName;
            dto.scores = this.scores;
            dto.averageScore = this.averageScore;
            dto.finalRating = this.finalRating;
            dto.comment = this.comment;
            return dto;
        }
    }
}
