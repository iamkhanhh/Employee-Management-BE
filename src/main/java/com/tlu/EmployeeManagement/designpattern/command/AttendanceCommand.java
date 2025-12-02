package com.tlu.EmployeeManagement.designpattern.command;

import com.tlu.EmployeeManagement.entity.Attendance;

public interface AttendanceCommand {
    Attendance execute();

    void undo();

    Integer getEmployeeId();
}
