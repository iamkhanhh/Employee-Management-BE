package com.tlu.EmployeeManagement.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.tlu.EmployeeManagement.dto.request.GetPresignedUrlForUploadDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.DashboardResponse;
import com.tlu.EmployeeManagement.dto.response.PresignedUrlResponse;
import com.tlu.EmployeeManagement.enums.UploadFolderType;
import com.tlu.EmployeeManagement.service.CommonService;
import com.tlu.EmployeeManagement.service.S3Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Tag(name = "Common", description = "Common APIs for file upload, dashboard statistics, and other utilities")
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonController {
    S3Service s3Service;
    CommonService commonService;

    @Operation(
        summary = "Generate presigned URL for upload",
        description = "Generate a presigned URL for uploading files to S3. The URL will be valid for a limited time and allows direct upload to S3."
    )
    @PostMapping("/generate-presigned-url")
    ApiResponse<PresignedUrlResponse> generateSinglePresignedUrl(HttpServletRequest request,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Presigned URL request data including file name and folder type",
                required = true,
                content = @Content(schema = @Schema(implementation = GetPresignedUrlForUploadDto.class))
            )
            @RequestBody GetPresignedUrlForUploadDto dto) {
        @SuppressWarnings("unchecked")

        Integer id = dto.getUserId();
        if (id == null) {
            Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
            id = Integer.parseInt(String.valueOf(user.get("id")));
        }

        PresignedUrlResponse presignedUrlResponse = s3Service.createPresignedUrlWithFolder(
            dto.getFileName(),
            id,
            dto.getFolderType().getFolderName()
        );

        ApiResponse<PresignedUrlResponse> apiResponse = ApiResponse.<PresignedUrlResponse>builder()
                                            .code(200)
                                            .status("success")
                                            .message("Generate presigned url for upload successfully")
                                            .data(presignedUrlResponse)
                                            .build();
        return apiResponse;
    }

    @Operation(
        summary = "Get admin dashboard statistics",
        description = "Get comprehensive dashboard statistics including overview stats, personnel by department, contract type distribution, salary by department, and employee count trend over the last 12 months. This endpoint is designed for admin dashboard visualization."
    )
    @GetMapping("/dashboard")
    public ApiResponse<DashboardResponse> getDashboard() {
        DashboardResponse dashboardData = commonService.getDashboardStats();

        return ApiResponse.<DashboardResponse>builder()
                .code(200)
                .status("success")
                .message("Dashboard statistics retrieved successfully")
                .data(dashboardData)
                .build();
    }
}
