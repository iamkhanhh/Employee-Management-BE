package com.tlu.EmployeeManagement.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tlu.EmployeeManagement.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isSwaggerRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/swagger-ui") ||
               path.startsWith("/api/v3/api-docs") ||
               path.startsWith("/api/swagger-resources") ||
               path.equals("/api/swagger-ui.html");
    }
    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException exception, HttpServletRequest request) {
        // Skip exception handling for Swagger endpoints
        if (isSwaggerRequest(request)) {
            throw new RuntimeException(exception);
        }

        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setStatus("failed");
        apiResponse.setMessage(exception.getMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        // Skip exception handling for Swagger endpoints - let Spring handle it
        if (isSwaggerRequest(request)) {
            throw new RuntimeException(exception);
        }

        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setStatus("failed");
        apiResponse.setMessage(exception.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingException(Exception exception, HttpServletRequest request) {
        // Skip exception handling for Swagger endpoints
        if (isSwaggerRequest(request)) {
            throw new RuntimeException(exception);
        }

        ApiResponse apiResponse = new ApiResponse<>();

        apiResponse.setStatus("failed");
        apiResponse.setMessage("Unknown error");
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
