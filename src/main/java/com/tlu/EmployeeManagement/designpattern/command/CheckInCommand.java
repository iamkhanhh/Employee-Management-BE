package com.tlu.EmployeeManagement.designpattern.command;

import com.tlu.EmployeeManagement.entity.Attendance;
import com.tlu.EmployeeManagement.service.AttendanceService;

/**
 * Concrete command for check-in operation
 * This is a Concrete Command in the Command Design Pattern
 * Delegates business logic to AttendanceService
 */
public class CheckInCommand implements AttendanceCommand {

    private final AttendanceService attendanceService;
    private final Integer employeeId;
    private Attendance executedAttendance;

    public CheckInCommand(AttendanceService attendanceService, Integer employeeId) {
        this.attendanceService = attendanceService;
        this.employeeId = employeeId;
    }

    @Override
    public Attendance execute() {
        executedAttendance = attendanceService.performCheckIn(employeeId);
        return executedAttendance;
    }

    @Override
    public void undo() {
        if (executedAttendance != null) {
            attendanceService.deleteAttendanceRecord(executedAttendance.getId());
            executedAttendance = null;
        }
    }

    @Override
    public Integer getEmployeeId() {
        return employeeId;
    }
}
