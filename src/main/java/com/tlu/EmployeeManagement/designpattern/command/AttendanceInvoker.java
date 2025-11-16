package com.tlu.EmployeeManagement.designpattern.command;

import java.util.Stack;

import org.springframework.stereotype.Component;

import com.tlu.EmployeeManagement.entity.Attendance;

@Component
public class AttendanceInvoker {

    private final Stack<AttendanceCommand> commandHistory = new Stack<>();

    public Attendance executeCommand(AttendanceCommand command) {
        Attendance result = command.execute();
        commandHistory.push(command);
        return result;
    }

    public boolean undoLastCommand() {
        if (!commandHistory.isEmpty()) {
            AttendanceCommand command = commandHistory.pop();
            command.undo();
            return true;
        }
        return false;
    }

    public boolean undoLastCommandForEmployee(Integer employeeId) {
        for (int i = commandHistory.size() - 1; i >= 0; i--) {
            AttendanceCommand command = commandHistory.get(i);
            if (command.getEmployeeId().equals(employeeId)) {
                command.undo();
                commandHistory.remove(i);
                return true;
            }
        }
        return false;
    }

    public void clearHistory() {
        commandHistory.clear();
    }

    public int getHistorySize() {
        return commandHistory.size();
    }
}
