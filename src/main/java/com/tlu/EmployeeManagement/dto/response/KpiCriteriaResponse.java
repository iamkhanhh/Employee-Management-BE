package com.tlu.EmployeeManagement.dto.response;

import java.math.BigDecimal;
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
@Schema(description = "Response object containing KPI evaluation criteria details")
public class KpiCriteriaResponse {

    @Schema(description = "Unique identifier of the KPI criteria", example = "1")
    Integer id;

    @Schema(description = "Name of the KPI criteria", example = "Work Quality")
    String name;

    @Schema(description = "Detailed description of the criteria", example = "Measures the quality and accuracy of work output")
    String description;

    @Schema(description = "Weight/importance of this criteria in final score (0-1)", example = "0.30")
    BigDecimal weight;

    @Schema(description = "Criteria creation timestamp", example = "10/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDateTime createdAt;

    @Schema(description = "Criteria last update timestamp", example = "12/11/2025")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    LocalDateTime updatedAt;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String name;
        private String description;
        private BigDecimal weight;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder weight(BigDecimal weight) {
            this.weight = weight;
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

        public KpiCriteriaResponse build() {
            KpiCriteriaResponse response = new KpiCriteriaResponse();
            response.id = this.id;
            response.name = this.name;
            response.description = this.description;
            response.weight = this.weight;
            response.createdAt = this.createdAt;
            response.updatedAt = this.updatedAt;
            return response;
        }
    }
}
