package com.tlu.EmployeeManagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    int code;
    String message;
    String status;
    T data;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int code;
        private String message;
        private String status;
        private T data;

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<T> status(String status) {
            this.status = status;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponse<T> build() {
            ApiResponse<T> response = new ApiResponse<>();
            response.code = this.code;
            response.message = this.message;
            response.status = this.status;
            response.data = this.data;
            return response;
        }
    }
}
