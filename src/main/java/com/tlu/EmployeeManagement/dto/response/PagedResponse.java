package com.tlu.EmployeeManagement.dto.response;

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
public class PagedResponse<T> {
    List<T> content;
    Integer currentPage;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    Boolean hasNext;
    Boolean hasPrevious;

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private List<T> content;
        private Integer currentPage;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean hasNext;
        private Boolean hasPrevious;

        public Builder<T> content(List<T> content) {
            this.content = content;
            return this;
        }

        public Builder<T> currentPage(Integer currentPage) {
            this.currentPage = currentPage;
            return this;
        }

        public Builder<T> pageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder<T> totalElements(Long totalElements) {
            this.totalElements = totalElements;
            return this;
        }

        public Builder<T> totalPages(Integer totalPages) {
            this.totalPages = totalPages;
            return this;
        }

        public Builder<T> hasNext(Boolean hasNext) {
            this.hasNext = hasNext;
            return this;
        }

        public Builder<T> hasPrevious(Boolean hasPrevious) {
            this.hasPrevious = hasPrevious;
            return this;
        }

        public PagedResponse<T> build() {
            PagedResponse<T> response = new PagedResponse<>();
            response.content = this.content;
            response.currentPage = this.currentPage;
            response.pageSize = this.pageSize;
            response.totalElements = this.totalElements;
            response.totalPages = this.totalPages;
            response.hasNext = this.hasNext;
            response.hasPrevious = this.hasPrevious;
            return response;
        }
    }
}
