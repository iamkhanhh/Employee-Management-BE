package com.tlu.EmployeeManagement.designpattern.command;

import java.util.ArrayList;
import java.util.List;

public class LeaveRequestInvoker {
    private final List<Command> history = new ArrayList<>();

    public void executeCommand(Command command) throws Exception {
        command.execute();
        history.add(command);
    }

    public List<Command> getHistory() {
        return history;
    }
}
