package com.tlu.EmployeeManagement.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.AttendanceResponse;
import com.tlu.EmployeeManagement.dto.response.EmployeeAttendanceStatusResponse;
import com.tlu.EmployeeManagement.dto.response.EmployeeResponse;
import com.tlu.EmployeeManagement.entity.Attendance;
import com.tlu.EmployeeManagement.service.AttendanceService;
import com.tlu.EmployeeManagement.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

/**
 * REST Controller for attendance operations
 * Uses Command Design Pattern through AttendanceService
 */
@Tag(name = "Attendance", description = "APIs for managing employee attendance (check-in/check-out)")
@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceController {

    AttendanceService attendanceService;
    EmployeeService employeeService;

    @Operation(
        summary = "Check in an employee",
        description = "Records the check-in time for the authenticated employee. Creates a new attendance record for today."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Check-in successful",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Bad request - Employee already checked in or invalid state",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/check-in")
    public ApiResponse<AttendanceResponse> checkIn(HttpServletRequest request) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
            Integer id = Integer.parseInt(String.valueOf(user.get("id")));

            EmployeeResponse employee = employeeService.getEmployeeByUserId(id);

            Attendance attendance = attendanceService.checkIn(employee.getId());
            AttendanceResponse response = attendanceService.toAttendanceResponse(attendance);

            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Check-in successful")
                .data(response)
                .build();

            return apiResponse;
        } catch (IllegalStateException e) {
            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("error")
                .message(e.getMessage())
                .build();
            return apiResponse;
        } catch (Exception e) {
            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("error")
                .message("Error during check-in: " + e.getMessage())
                .build();
            return apiResponse;
        }
    }

    @Operation(
        summary = "Check out an employee",
        description = "Records the check-out time for the authenticated employee. Updates the existing attendance record."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Check-out successful",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "400",
        description = "Bad request - Employee not checked in or invalid state",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/check-out")
    public ApiResponse<AttendanceResponse> checkOut(HttpServletRequest request) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
            Integer id = Integer.parseInt(String.valueOf(user.get("id")));

            EmployeeResponse employee = employeeService.getEmployeeByUserId(id);
            Attendance attendance = attendanceService.checkOut(employee.getId());

            AttendanceResponse response = attendanceService.toAttendanceResponse(attendance);

            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Check-out successful")
                .data(response)
                .build();

            return apiResponse;
        } catch (IllegalStateException e) {
            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .status("error")
                .message(e.getMessage())
                .build();
            return apiResponse;
        } catch (Exception e) {
            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("error")
                .message("Error during check-out: " + e.getMessage())
                .build();
            return apiResponse;
        }
    }

    @Operation(
        summary = "Undo last attendance operation",
        description = "Undoes the last check-in or check-out operation for the authenticated employee using the Command pattern."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Operation undone successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "No operation to undo"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error"
    )
    @PostMapping("/undo")
    public ApiResponse<Void> undoLastOperation(HttpServletRequest request) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
            Integer id = Integer.parseInt(String.valueOf(user.get("id")));
            EmployeeResponse employee = employeeService.getEmployeeByUserId(id);
            boolean success = attendanceService.undoLastOperation(employee.getId());

            if (success) {
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .status("success")
                    .message("Last operation undone successfully")
                    .build();
                return apiResponse;
            } else {
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .status("error")
                    .message("No operation to undo for this employee")
                    .build();
                return apiResponse;
            }
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("error")
                .message("Error during undo: " + e.getMessage())
                .build();
            return apiResponse;
        }
    }

    @Operation(
        summary = "Get attendance record by ID",
        description = "Retrieves a specific attendance record by its ID."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Attendance record retrieved successfully",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "404",
        description = "Attendance record not found",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @GetMapping("/{id}")
    public ApiResponse<AttendanceResponse> getAttendanceById(
            @Parameter(description = "Attendance record ID", required = true)
            @PathVariable Integer id) {
        try {
            Attendance attendance = attendanceService.getAttendanceById(id);
            AttendanceResponse response = attendanceService.toAttendanceResponse(attendance);

            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Attendance record retrieved successfully")
                .data(response)
                .build();

            return apiResponse;
        } catch (IllegalArgumentException e) {
            ApiResponse<AttendanceResponse> apiResponse = ApiResponse.<AttendanceResponse>builder()
                .code(HttpStatus.NOT_FOUND.value())
                .status("error")
                .message(e.getMessage())
                .build();
            return apiResponse;
        }
    }
    
    @Operation(
        summary = "Get employee attendance status for today by department",
        description = "Returns attendance status (PRESENT, ABSENT, LATE) for all employees in a department for today. " +
                     "If no department ID is provided, returns status for all employees. " +
                     "Shows check-in/check-out times and calculates late minutes if applicable."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Employee attendance status retrieved successfully",
        content = @Content(schema = @Schema(implementation = EmployeeAttendanceStatusResponse.class))
    )
    @GetMapping("/department")
    public ApiResponse<List<EmployeeAttendanceStatusResponse>> getEmployeeAttendanceStatusToday(
            @Parameter(description = "Department ID (optional). If not provided, returns all employees", required = false)
            @RequestParam(required = false) Integer deptId) {
        List<EmployeeAttendanceStatusResponse> statuses = attendanceService.getEmployeeAttendanceStatusToday(deptId);

        ApiResponse<List<EmployeeAttendanceStatusResponse>> apiResponse =
            ApiResponse.<List<EmployeeAttendanceStatusResponse>>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Employee attendance status retrieved successfully")
                .data(statuses)
                .build();

        return apiResponse;
    }

    @Operation(
        summary = "Get attendance records for current employee",
        description = "Returns attendance records for the authenticated employee. " +
                     "Defaults to current month and year if not specified."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Attendance records retrieved successfully",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @GetMapping("/me")
    public ApiResponse<List<AttendanceResponse>> getMyAttendance(
            HttpServletRequest request,
            @Parameter(description = "Month (1-12). Defaults to current month", example = "11")
            @RequestParam(required = false) Integer month,
            @Parameter(description = "Year (e.g., 2024). Defaults to current year", example = "2024")
            @RequestParam(required = false) Integer year) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
        Integer id = Integer.parseInt(String.valueOf(user.get("id")));
        EmployeeResponse employee = employeeService.getEmployeeByUserId(id);
        Integer employeeId = employee.getId();

        // Default to current month and year if not provided
        LocalDateTime now = LocalDateTime.now();
        int filterMonth = (month != null) ? month : now.getMonthValue();
        int filterYear = (year != null) ? year : now.getYear();

        List<Attendance> attendances = attendanceService.getAttendanceByEmployeeIdAndMonth(
            employeeId, filterMonth, filterYear);

        List<AttendanceResponse> responses = attendances.stream()
            .map(attendanceService::toAttendanceResponse)
            .collect(Collectors.toList());

        ApiResponse<List<AttendanceResponse>> apiResponse = ApiResponse.<List<AttendanceResponse>>builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("Attendance records retrieved successfully")
            .data(responses)
            .build();

        return apiResponse;
    }

    @Operation(
        summary = "Get attendance records for a specific employee",
        description = "Returns attendance records for the specified employee. " +
                     "Defaults to current month and year if not specified."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Attendance records retrieved successfully",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @GetMapping("/employee/{employeeId}")
    public ApiResponse<List<AttendanceResponse>> getAttendanceByEmployeeId(
            @Parameter(description = "Employee ID", required = true, example = "1")
            @PathVariable Integer employeeId,
            @Parameter(description = "Month (1-12). Defaults to current month", example = "11")
            @RequestParam(required = false) Integer month,
            @Parameter(description = "Year (e.g., 2024). Defaults to current year", example = "2024")
            @RequestParam(required = false) Integer year) {

        // Default to current month and year if not provided
        LocalDateTime now = LocalDateTime.now();
        int filterMonth = (month != null) ? month : now.getMonthValue();
        int filterYear = (year != null) ? year : now.getYear();

        List<Attendance> attendances = attendanceService.getAttendanceByEmployeeIdAndMonth(
            employeeId, filterMonth, filterYear);

        List<AttendanceResponse> responses = attendances.stream()
            .map(attendanceService::toAttendanceResponse)
            .collect(Collectors.toList());

        ApiResponse<List<AttendanceResponse>> apiResponse = ApiResponse.<List<AttendanceResponse>>builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("Attendance records retrieved successfully")
            .data(responses)
            .build();

        return apiResponse;
    }

    @Operation(
        summary = "Get all attendance records",
        description = "Returns all attendance records for all employees. " +
                     "Defaults to current month and year if not specified."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Attendance records retrieved successfully",
        content = @Content(schema = @Schema(implementation = AttendanceResponse.class))
    )
    @GetMapping
    public ApiResponse<List<AttendanceResponse>> getAllAttendance(
            @Parameter(description = "Month (1-12). Defaults to current month", example = "11")
            @RequestParam(required = false) Integer month,
            @Parameter(description = "Year (e.g., 2024). Defaults to current year", example = "2024")
            @RequestParam(required = false) Integer year) {

        // Default to current month and year if not provided
        LocalDateTime now = LocalDateTime.now();
        int filterMonth = (month != null) ? month : now.getMonthValue();
        int filterYear = (year != null) ? year : now.getYear();

        List<Attendance> attendances = attendanceService.getAttendanceByMonth(filterMonth, filterYear);

        List<AttendanceResponse> responses = attendances.stream()
            .map(attendanceService::toAttendanceResponse)
            .collect(Collectors.toList());

        ApiResponse<List<AttendanceResponse>> apiResponse = ApiResponse.<List<AttendanceResponse>>builder()
            .code(HttpStatus.OK.value())
            .status("success")
            .message("Attendance records retrieved successfully")
            .data(responses)
            .build();

        return apiResponse;
    }

    @Operation(
        summary = "Delete an attendance record",
        description = "Deletes a specific attendance record by its ID."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Attendance record deleted successfully"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "500",
        description = "Internal server error"
    )
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAttendance(
            @Parameter(description = "Attendance record ID", required = true, example = "1")
            @PathVariable Integer id) {
        try {
            attendanceService.deleteAttendance(id);
            ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .status("success")
                .message("Attendance record deleted successfully")
                .build();
            return apiResponse;
        } catch (Exception e) {
            ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status("error")
                .message("Error deleting attendance: " + e.getMessage())
                .build();
            return apiResponse;
        }
    }
}
