package com.tlu.EmployeeManagement.dto.response;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PagedResponse<T> {
    List<T> content;
    Integer currentPage;
    Integer pageSize;
    Long totalElements;
    Integer totalPages;
    Boolean hasNext;
    Boolean hasPrevious;
}
