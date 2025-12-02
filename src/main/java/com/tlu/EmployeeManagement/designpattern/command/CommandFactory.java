package com.tlu.EmployeeManagement.designpattern.command;

import org.springframework.stereotype.Component;

import com.tlu.EmployeeManagement.enums.CommandType;
import com.tlu.EmployeeManagement.service.AttendanceService;
import com.tlu.EmployeeManagement.service.LeaveRequestService;

import jakarta.annotation.PostConstruct;

@Component
public class CommandFactory {
    private final LeaveRequestService leaveRequestService;
    private final AttendanceService attendanceService;

    public CommandFactory(LeaveRequestService leaveRequestService, AttendanceService attendanceService) {
        this.leaveRequestService = leaveRequestService;
        this.attendanceService = attendanceService;
    }

    @PostConstruct
    private void init() {
        attendanceService.setCommandFactory(this);
    }

    /**
     * Create an approve leave command
     * @param leaveId The leave request ID
     * @return ApproveLeaveCommand instance
     */
    public Command createApproveCommand(Integer leaveId) {
        return new ApproveLeaveCommand(leaveRequestService, leaveId);
    }

    /**
     * Create a reject leave command
     * @param leaveId The leave request ID
     * @param rejectReason The rejection reason
     * @return RejectLeaveCommand instance
     */
    public Command createRejectCommand(Integer leaveId, String rejectReason) {
        return new RejectLeaveCommand(leaveRequestService, leaveId, rejectReason);
    }

    /**
     * Create a check-in command for an employee
     * @param employeeId The employee ID
     * @return CheckInCommand instance
     */
    public AttendanceCommand createCheckInCommand(Integer employeeId) {
        return new CheckInCommand(attendanceService, employeeId);
    }

    /**
     * Create a check-out command for an employee
     * @param employeeId The employee ID
     * @return CheckOutCommand instance
     */
    public AttendanceCommand createCheckOutCommand(Integer employeeId) {
        return new CheckOutCommand(attendanceService, employeeId);
    }

    /**
     * Create a command based on command type enum (simplified version for attendance commands)
     * @param commandType The type of command (CHECKIN or CHECKOUT only)
     * @param employeeId The employee ID
     * @return AttendanceCommand instance
     * @throws IllegalArgumentException if command type is invalid or parameters are missing
     */
    public AttendanceCommand createAttendanceCommand(CommandType commandType, Integer employeeId) {
        if (commandType == null) {
            throw new IllegalArgumentException("Command type cannot be null");
        }
        if (employeeId == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        switch (commandType) {
            case CHECKIN:
                return createCheckInCommand(employeeId);
            case CHECKOUT:
                return createCheckOutCommand(employeeId);
            default:
                throw new IllegalArgumentException("Invalid attendance command type: " + commandType + ". Expected CHECKIN or CHECKOUT");
        }
    }
}
