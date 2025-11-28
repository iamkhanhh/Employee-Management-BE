package com.tlu.EmployeeManagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.EmployeeManagement.dto.request.EmployeeReviewSubmitDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodCreateDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodFilterDto;
import com.tlu.EmployeeManagement.dto.request.KpiPeriodUpdateDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.EmployeeReviewRecordDto;
import com.tlu.EmployeeManagement.dto.response.KpiCriteriaResponse;
import com.tlu.EmployeeManagement.dto.response.KpiPeriodResponse;
import com.tlu.EmployeeManagement.dto.response.KpiResultResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Tag(name = "Review", description = "APIs for managing KPI periods and employee reviews")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @Operation(summary = "Get all KPI periods", description = "Retrieve a paginated list of KPI periods with optional filtering by period name, start date, and end date")
    @GetMapping("/periods")
    public ApiResponse<PagedResponse<KpiPeriodResponse>> getKpiPeriods(
            @Parameter(description = "Page number (zero-based)", example = "0") @RequestParam(required = false, defaultValue = "0") Integer page,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @Parameter(description = "Filter by period name") @RequestParam(required = false) String periodName,
            @Parameter(description = "Filter by start date (format: dd/MM/yyyy)", example = "01/01/2024") @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @Parameter(description = "Filter by end date (format: dd/MM/yyyy)", example = "31/12/2024") @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {

        KpiPeriodFilterDto filterDto = KpiPeriodFilterDto.builder()
            .page(page)
            .pageSize(pageSize)
            .periodName(periodName)
            .startDate(startDate)
            .endDate(endDate)
            .build();

        PagedResponse<KpiPeriodResponse> periods = reviewService.getKpiPeriods(filterDto);

        ApiResponse<PagedResponse<KpiPeriodResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get KPI periods successfully");
        apiResponse.setData(periods);
        return apiResponse;
    }

    @Operation(summary = "Get KPI period by ID", description = "Retrieve a single KPI period by its ID")
    @GetMapping("/periods/{id}")
    public ApiResponse<KpiPeriodResponse> getKpiPeriodById(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer id) {
        KpiPeriodResponse period = reviewService.getKpiPeriodById(id);

        ApiResponse<KpiPeriodResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get KPI period successfully");
        apiResponse.setData(period);
        return apiResponse;
    }

    @Operation(summary = "Create KPI period", description = "Create a new KPI period (Admin only)")
    @PostMapping("/periods")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<KpiPeriodResponse> createKpiPeriod(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "KPI period creation data", required = true)
            @Valid @RequestBody KpiPeriodCreateDto createDto) {
        KpiPeriodResponse period = reviewService.createKpiPeriod(createDto);

        ApiResponse<KpiPeriodResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("KPI period created successfully");
        apiResponse.setData(period);
        return apiResponse;
    }

    @Operation(summary = "Update KPI period", description = "Update an existing KPI period (Admin only)")
    @PutMapping("/periods/{id}")
    public ApiResponse<KpiPeriodResponse> updateKpiPeriod(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "KPI period update data", required = true)
            @Valid @RequestBody KpiPeriodUpdateDto updateDto) {
        KpiPeriodResponse period = reviewService.updateKpiPeriod(id, updateDto);

        ApiResponse<KpiPeriodResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("KPI period updated successfully");
        apiResponse.setData(period);
        return apiResponse;
    }

    @Operation(summary = "Delete KPI period", description = "Soft delete a KPI period (Admin only)")
    @DeleteMapping("/periods/{id}")
    public ApiResponse<Void> deleteKpiPeriod(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer id) {
        reviewService.deleteKpiPeriod(id);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("KPI period deleted successfully");
        return apiResponse;
    }

    @Operation(summary = "Get employee review records for a period", description = "Retrieve all employee review records with scores, average, and final rating for a specific KPI period. Admin can see all employees, department heads can see employees in their department, regular employees can only see their own records.")
    @GetMapping("/periods/{periodId}/reviews")
    public ApiResponse<List<EmployeeReviewRecordDto>> getEmployeeReviewRecords(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer periodId) {
        List<EmployeeReviewRecordDto> records = reviewService.getEmployeeReviewRecords(periodId);

        ApiResponse<List<EmployeeReviewRecordDto>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get employee review records successfully");
        apiResponse.setData(records);
        return apiResponse;
    }

    @Operation(summary = "Get single employee review record", description = "Retrieve a single employee review record by KPI result ID for a specific period. Admin can see all reviews, department heads can see reviews in their department, regular employees can only see their own review.")
    @GetMapping("/periods/{periodId}/reviews/{reviewId}")
    public ApiResponse<EmployeeReviewRecordDto> getEmployeeReviewRecordById(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer periodId,
            @Parameter(description = "KPI result ID (review ID)", required = true, example = "1") @PathVariable Integer reviewId) {
        EmployeeReviewRecordDto record = reviewService.getEmployeeReviewRecordById(periodId, reviewId);

        ApiResponse<EmployeeReviewRecordDto> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get employee review record successfully");
        apiResponse.setData(record);
        return apiResponse;
    }

    @Operation(summary = "Submit employee review", description = "Submit KPI scores for an employee in a specific period. Only admin and department heads can submit reviews. Department heads can only review employees in their own department.")
    @PostMapping("/periods/{periodId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<KpiResultResponse> submitEmployeeReview(
            @Parameter(description = "KPI period ID", required = true, example = "1") @PathVariable Integer periodId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Employee review submission data", required = true)
            @Valid @RequestBody EmployeeReviewSubmitDto submitDto) {
        KpiResultResponse result = reviewService.submitEmployeeReview(periodId, submitDto);

        ApiResponse<KpiResultResponse> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Employee review submitted successfully");
        apiResponse.setData(result);
        return apiResponse;
    }

    @Operation(summary = "Get all KPI criteria", description = "Retrieve all active KPI criteria with their weights")
    @GetMapping("/criteria")
    public ApiResponse<List<KpiCriteriaResponse>> getAllKpiCriteria() {
        List<KpiCriteriaResponse> criteria = reviewService.getAllKpiCriteria();

        ApiResponse<List<KpiCriteriaResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get KPI criteria successfully");
        apiResponse.setData(criteria);
        return apiResponse;
    }

    @Operation(summary = "Get current user's KPI reviews", description = "Retrieve all KPI reviews for the currently authenticated user")
    @GetMapping("/me")
    public ApiResponse<List<KpiResultResponse>> getCurrentUserReviews() {
        List<KpiResultResponse> reviews = reviewService.getCurrentUserReviews();

        ApiResponse<List<KpiResultResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Get current user reviews successfully");
        apiResponse.setData(reviews);
        return apiResponse;
    }
}
