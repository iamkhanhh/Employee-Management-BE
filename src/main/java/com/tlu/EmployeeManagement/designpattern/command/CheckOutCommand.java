package com.tlu.EmployeeManagement.designpattern.command;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.tlu.EmployeeManagement.entity.Attendance;
import com.tlu.EmployeeManagement.service.AttendanceService;

public class CheckOutCommand implements AttendanceCommand {

    private final AttendanceService attendanceService;
    private final Integer employeeId;
    private Attendance executedAttendance;
    private LocalDateTime previousCheckOut;
    private BigDecimal previousOvertimeHours;

    public CheckOutCommand(AttendanceService attendanceService, Integer employeeId) {
        this.attendanceService = attendanceService;
        this.employeeId = employeeId;
    }

    @Override
    public Attendance execute() {
        Attendance activeAttendance = attendanceService.getActiveAttendance(employeeId);
        if (activeAttendance != null) {
            previousCheckOut = activeAttendance.getCheckOut();
            previousOvertimeHours = activeAttendance.getOvertimeHours();
        }

        // Delegate business logic to service layer
        executedAttendance = attendanceService.performCheckOut(employeeId);
        return executedAttendance;
    }

    @Override
    public void undo() {
        if (executedAttendance != null) {
            attendanceService.restoreCheckOutState(
                executedAttendance.getId(),
                previousCheckOut,
                previousOvertimeHours
            );
            executedAttendance = null;
        }
    }

    @Override
    public Integer getEmployeeId() {
        return employeeId;
    }
}
