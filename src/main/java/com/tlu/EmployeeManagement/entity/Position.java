package com.tlu.EmployeeManagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "positions")
@Data
@EqualsAndHashCode(callSuper = true)
public class Position extends AbtractEntity {

    @Column(name = "position_name", length = 100)
    private String positionName;

    @Column(length = 255)
    private String description;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String positionName;
        private String description;

        public Builder positionName(String positionName) {
            this.positionName = positionName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Position build() {
            Position position = new Position();
            position.positionName = this.positionName;
            position.description = this.description;
            return position;
        }
    }
}
