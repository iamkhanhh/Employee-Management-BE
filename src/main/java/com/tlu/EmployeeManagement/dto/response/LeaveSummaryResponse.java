package com.tlu.EmployeeManagement.dto.response;

import com.tlu.EmployeeManagement.enums.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;




@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveSummaryResponse {
    long total;
    long pending;
    long approved;
    long rejected;
}
