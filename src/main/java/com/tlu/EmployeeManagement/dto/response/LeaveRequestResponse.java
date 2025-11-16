package com.tlu.EmployeeManagement.dto.response;


import java.time.LocalDate;
import com.tlu.EmployeeManagement.enums.LeaveStatus;
import com.tlu.EmployeeManagement.enums.LeaveType;
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
public class LeaveRequestResponse {
    Integer id;
    Integer empId;
    String employeeName;
    LeaveType leaveType;
    LocalDate startDate;
    LocalDate endDate;
    Integer daysRequested;
    String reason;
    LeaveStatus status;
    Integer approvedBy;
    String approverName;
    LocalDate approvedDate;
    String rejectReason;
}
