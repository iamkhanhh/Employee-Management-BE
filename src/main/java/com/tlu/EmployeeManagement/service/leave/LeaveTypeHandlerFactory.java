package com.tlu.EmployeeManagement.service.leave;

import com.tlu.EmployeeManagement.enums.LeaveType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LeaveTypeHandlerFactory {
    private final List<LeaveTypeHandler> handlers;

    public LeaveTypeHandlerFactory(List<LeaveTypeHandler> handlers) {
        this.handlers = handlers;
    }

    public LeaveTypeHandler getHandler(LeaveType type) {
        return handlers.stream()
                .filter(h -> h.getType() == type)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler for leave type: " + type));
    }
}
    

