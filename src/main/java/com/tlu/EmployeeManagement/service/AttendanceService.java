package com.tlu.EmployeeManagement.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import com.tlu.EmployeeManagement.designpattern.command.AttendanceCommand;
import com.tlu.EmployeeManagement.designpattern.command.AttendanceInvoker;
import com.tlu.EmployeeManagement.designpattern.command.CommandFactory;
import com.tlu.EmployeeManagement.dto.response.AttendanceResponse;
import com.tlu.EmployeeManagement.dto.response.EmployeeAttendanceStatusResponse;
import com.tlu.EmployeeManagement.entity.Attendance;
import com.tlu.EmployeeManagement.entity.Employee;
import com.tlu.EmployeeManagement.enums.EmployeeStatus;
import com.tlu.EmployeeManagement.repository.AttendanceRepository;
import com.tlu.EmployeeManagement.repository.EmployeeRepository;
import com.tlu.EmployeeManagement.enums.CommandType;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

/**
 * Service class for attendance operations
 * Uses Command Design Pattern for check-in and check-out operations
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceInvoker attendanceInvoker;

    @NonFinal
    @Value("${attendance.workStartTime:08:00}")
    private LocalTime workStartTime;

    @NonFinal
    @Value("${attendance.standardWorkHoursPerDay:8}")
    private double standardWorkHoursPerDay;

    private CommandFactory commandFactory;

    /**
     * Setter for CommandFactory to avoid circular dependency
     * @param commandFactory The command factory instance
     */
    public void setCommandFactory(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    /**
     * Check in an employee using Command pattern
     * @param employeeId The employee ID
     * @return The created attendance record
     */
    public Attendance checkIn(Integer employeeId) {
        AttendanceCommand command = commandFactory.createAttendanceCommand(CommandType.CHECKIN, employeeId);
        return attendanceInvoker.executeCommand(command);
    }

    /**
     * Check out an employee using Command pattern
     * @param employeeId The employee ID
     * @return The updated attendance record
     */
    public Attendance checkOut(Integer employeeId) {
        AttendanceCommand command = commandFactory.createAttendanceCommand(CommandType.CHECKOUT, employeeId);
        return attendanceInvoker.executeCommand(command);
    }

    /**
     * Business logic: Create a new check-in attendance record
     * @param employeeId The employee ID
     * @return The created attendance record
     */
    public Attendance performCheckIn(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

        if (employee.getIsDeleted() || employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Employee is not active and cannot check in");
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        var todayAttendance = attendanceRepository.findAttendanceForToday(employeeId, startOfDay, endOfDay);
        if (todayAttendance.isPresent()) {
            throw new IllegalStateException("Employee has already checked in today. Only one check-in per day is allowed.");
        }

        Attendance attendance = Attendance.builder()
                .empId(employeeId)
                .checkIn(LocalDateTime.now())
                .build();

        System.out.println("[DEBUG] CheckIn before save: " + attendance.getCheckIn());

        attendance = attendanceRepository.save(attendance);

        System.out.println("[DEBUG] CheckIn after save: " + attendance.getCheckIn());

        return attendance;
    }

    /**
     * Business logic: Update attendance record with check-out time and calculate overtime
     * @param employeeId The employee ID
     * @return The updated attendance record
     */
    public Attendance performCheckOut(Integer employeeId) {
        // Validate employee exists and is active
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with id: " + employeeId));

        if (employee.getIsDeleted() || employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new IllegalStateException("Employee is not active and cannot check out");
        }

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        Attendance activeAttendance = attendanceRepository.findAttendanceForToday(employeeId, startOfDay, endOfDay)
                .orElseThrow(() -> new IllegalStateException("No active check-in found. Cannot check out."));

        LocalDateTime checkOutTime = LocalDateTime.now();
        activeAttendance.setCheckOut(checkOutTime);

        BigDecimal overtimeHours = calculateOvertimeHours(activeAttendance.getCheckIn(), checkOutTime);
        activeAttendance.setOvertimeHours(overtimeHours);

        return attendanceRepository.save(activeAttendance);
    }

    /**
     * Business logic: Delete an attendance record (for undo operation)
     * @param attendanceId The attendance ID
     */
    public void deleteAttendanceRecord(Integer attendanceId) {
        attendanceRepository.deleteById(attendanceId);
    }

    /**
     * Business logic: Restore previous check-out state (for undo operation)
     * @param attendanceId The attendance ID
     * @param previousCheckOut Previous check-out time
     * @param previousOvertimeHours Previous overtime hours
     * @return The updated attendance record
     */
    public Attendance restoreCheckOutState(Integer attendanceId, LocalDateTime previousCheckOut, BigDecimal previousOvertimeHours) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found with id: " + attendanceId));

        attendance.setCheckOut(previousCheckOut);
        attendance.setOvertimeHours(previousOvertimeHours);

        return attendanceRepository.save(attendance);
    }

    /**
     * Calculate overtime hours based on check-in and check-out times
     * @param checkIn Check-in time
     * @param checkOut Check-out time
     * @return Overtime hours as BigDecimal
     */
    private BigDecimal calculateOvertimeHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        Duration duration = Duration.between(checkIn, checkOut);
        double totalHours = duration.toMinutes() / 60.0;
        double overtimeHours = Math.max(0, totalHours - standardWorkHoursPerDay);
        return BigDecimal.valueOf(overtimeHours).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Undo the last attendance operation for an employee
     * @param employeeId The employee ID
     * @return true if undo was successful, false otherwise
     */
    public boolean undoLastOperation(Integer employeeId) {
        return attendanceInvoker.undoLastCommandForEmployee(employeeId);
    }

    /**
     * Get attendance record by ID
     * @param id The attendance ID
     * @return Attendance record
     */
    public Attendance getAttendanceById(Integer id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attendance not found with id: " + id));
    }

    /**
     * Get all attendance records for an employee
     * @param employeeId The employee ID
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByEmployeeId(Integer employeeId) {
        return attendanceRepository.findByEmpId(employeeId);
    }

    /**
     * Get attendance records for an employee within a date range
     * @param employeeId The employee ID
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByEmployeeIdAndDateRange(
            Integer employeeId,
            LocalDateTime startDate,
            LocalDateTime endDate) {
        return attendanceRepository.findByEmpIdAndDateRange(employeeId, startDate, endDate);
    }

    /**
     * Get all attendance records within a date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return attendanceRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Get active (not checked out) attendance for an employee
     * @param employeeId The employee ID
     * @return Active attendance record if exists, null otherwise
     */
    public Attendance getActiveAttendance(Integer employeeId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        return attendanceRepository.findAttendanceForToday(employeeId, startOfDay, endOfDay).orElse(null);
    }

    /**
     * Delete an attendance record
     * @param id The attendance ID
     */
    public void deleteAttendance(Integer id) {
        attendanceRepository.deleteById(id);
    }

    /**
     * Get all attendance records
     * @return List of all attendance records
     */
    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    /**
     * Get attendance records for an employee filtered by month
     * @param employeeId The employee ID
     * @param month Month (1-12)
     * @param year Year (e.g., 2024)
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByEmployeeIdAndMonth(Integer employeeId, Integer month, Integer year) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        return attendanceRepository.findByEmpIdAndDateRange(employeeId, startDate, endDate);
    }

    /**
     * Get all attendance records filtered by month
     * @param month Month (1-12)
     * @param year Year (e.g., 2024)
     * @return List of attendance records
     */
    public List<Attendance> getAttendanceByMonth(Integer month, Integer year) {
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1);
        return attendanceRepository.findByDateRange(startDate, endDate);
    }

    /**
     * Convert Attendance entity to AttendanceResponse DTO
     * @param attendance The attendance entity
     * @return AttendanceResponse DTO
     */
    public AttendanceResponse toAttendanceResponse(Attendance attendance) {
        return AttendanceResponse.builder()
            .id(attendance.getId())
            .empId(attendance.getEmpId())
            .checkIn(attendance.getCheckIn())
            .checkOut(attendance.getCheckOut())
            .overtimeHours(attendance.getOvertimeHours())
            .createdAt(attendance.getCreatedAt())
            .updatedAt(attendance.getUpdatedAt())
            .build();
    }

    /**
     * Get employee attendance status for today by department
     * @param deptId Department ID (optional, if null returns all employees)
     * @return List of employee attendance status
     */
    public List<EmployeeAttendanceStatusResponse> getEmployeeAttendanceStatusToday(Integer deptId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<Employee> employees;
        if (deptId != null) {
            employees = employeeRepository.findByDeptIdAndStatus(deptId, EmployeeStatus.ACTIVE);
        } else {
            employees = employeeRepository.findAll().stream()
                .filter(e -> !e.getIsDeleted() && e.getStatus() == EmployeeStatus.ACTIVE)
                .collect(Collectors.toList());
        }

        List<Integer> employeeIds = employees.stream()
            .map(Employee::getId)
            .collect(Collectors.toList());

        List<Attendance> attendances = attendanceRepository.findByEmpIdsAndDateRange(
            employeeIds, startOfDay, endOfDay);

        Map<Integer, Attendance> attendanceMap = attendances.stream()
            .collect(Collectors.toMap(Attendance::getEmpId, a -> a, (a1, a2) -> a1));

        return employees.stream()
            .map(employee -> buildEmployeeAttendanceStatus(employee, attendanceMap.get(employee.getId())))
            .collect(Collectors.toList());
    }

    /**
     * Build employee attendance status response
     * @param employee The employee
     * @param attendance The attendance record (can be null)
     * @return EmployeeAttendanceStatusResponse
     */
    private EmployeeAttendanceStatusResponse buildEmployeeAttendanceStatus(Employee employee, Attendance attendance) {
        if (attendance == null) {
            return EmployeeAttendanceStatusResponse.builder()
                .employeeId(employee.getId())
                .fullName(employee.getFullName())
                .status("ABSENT")
                .checkInTime(null)
                .checkOutTime(null)
                .isLate(false)
                .lateMinutes(null)
                .build();
        }

        LocalDateTime checkInTime = attendance.getCheckIn();
        LocalTime checkInLocalTime = checkInTime.toLocalTime();

        boolean isLate = checkInLocalTime.isAfter(workStartTime);
        String lateMinutes = null;

        if (isLate) {
            long minutes = Duration.between(workStartTime, checkInLocalTime).toMinutes();
            lateMinutes = minutes + " minutes";
        }

        return EmployeeAttendanceStatusResponse.builder()
            .employeeId(employee.getId())
            .fullName(employee.getFullName())
            .status(isLate ? "LATE" : "PRESENT")
            .checkInTime(checkInTime)
            .checkOutTime(attendance.getCheckOut())
            .isLate(isLate)
            .lateMinutes(lateMinutes)
            .build();
    }
}
