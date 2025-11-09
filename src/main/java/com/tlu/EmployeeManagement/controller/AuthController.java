package com.tlu.EmployeeManagement.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tlu.EmployeeManagement.dto.request.ForgotPasswordDto;
import com.tlu.EmployeeManagement.dto.request.LoginDto;
import com.tlu.EmployeeManagement.dto.request.VerifiDto;
import com.tlu.EmployeeManagement.dto.request.RegisterUserDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.AuthResponse;
import com.tlu.EmployeeManagement.dto.response.UserResponse;
import com.tlu.EmployeeManagement.service.AuthService;
// import com.tlu.EmployeeManagement.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Tag(name = "Authentication", description = "APIs for user authentication and authorization")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    AuthService authService;
    // UserService userService;

    @Operation(
        summary = "Login",
        description = "Authenticate user with email and password. Returns JWT token in HttpOnly cookie (access_token) with 5 days expiry."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Login successful",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/login")
    public ApiResponse<AuthResponse> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Login credentials (email and password)",
                required = true,
                content = @Content(schema = @Schema(implementation = LoginDto.class))
            )
            @RequestBody LoginDto request,
            HttpServletResponse response) {
        String token = authService.authenticate(request);

        Cookie jwtCookie = new Cookie("access_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24 * 5);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<AuthResponse>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Login successfilly");
        return apiResponse;
    }

    @Operation(
        summary = "Logout",
        description = "Logout user by clearing the access_token cookie. Sets cookie maxAge to 0 to delete it from browser."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "Logout successful",
        content = @Content(schema = @Schema(implementation = ApiResponse.class))
    )
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("access_token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Logout successfully");
        return apiResponse;
    }

    // @GetMapping("/me")
    // public ResponseEntity<UserResponse> getUserById(HttpServletRequest request) {
    //     UserResponse userResponse = userService.getUserInfo(request);
    //     return ResponseEntity.ok(userResponse);
    // }


    // @PostMapping("/signup")
    // ApiResponse<AuthResponse> register(@RequestBody RegisterUserDto registerUserDto, HttpServletResponse response) {
    //     String token = authService.signup(registerUserDto);

    //     Cookie jwtCookie = new Cookie("access_token", token);
    //     jwtCookie.setHttpOnly(true);
    //     jwtCookie.setSecure(false); // Để chạy trên localhost, nếu production thì phải `true`
    //     jwtCookie.setPath("/");
    //     jwtCookie.setMaxAge(60 * 60 * 24 * 5);
    //     jwtCookie.setAttribute("SameSite", "Strict");

    //     response.addCookie(jwtCookie);

    //     ApiResponse<AuthResponse> apiResponse = new ApiResponse<AuthResponse>();

    //     apiResponse.setStatus("success");
    //     apiResponse.setMessage("Signup successfilly");
    //     return apiResponse;
    // }

    // @PostMapping("/verify")
    // public ApiResponse<Void> verifyUser(@RequestBody VerifiDto verifyUserDto) {
    //     try {
    //         authService.verifyUser(verifyUserDto);

    //         ApiResponse<Void> apiResponse = new ApiResponse<>();
    //         apiResponse.setStatus("success");
    //         apiResponse.setMessage("Account verified successfully");

    //         return apiResponse;
    //     } catch (RuntimeException e) {
    //         ApiResponse<Void> errorResponse = new ApiResponse<>();
    //         errorResponse.setStatus("failed");
    //         errorResponse.setMessage(e.getMessage());

    //         return errorResponse;
    //     }
    // }

    // @PostMapping("/resend")
    // public ApiResponse<Void> resendVerificationCode(@RequestParam String email) {
    //     try {
    //         authService.resendVerificationCode(email);

    //         ApiResponse<Void> apiResponse = new ApiResponse<>();
    //         apiResponse.setStatus("success");
    //         apiResponse.setMessage("Verification code sent");

    //         return apiResponse;
    //     } catch (RuntimeException e) {
    //         ApiResponse<Void> errorResponse = new ApiResponse<>();
    //         errorResponse.setStatus("failed");
    //         errorResponse.setMessage(e.getMessage());

    //         return errorResponse;
    //     }
    // }

    // @PostMapping("/forgot_password")
    // public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordDto input) {
    //     try {
    //         authService.forgotPassword(input);

    //         ApiResponse<Void> successResponse = new ApiResponse<>();
    //         successResponse.setStatus("success");
    //         successResponse.setMessage("Change Password Successfully");

    //         return successResponse;
    //     } catch (RuntimeException e) {
    //         ApiResponse<Void> errorResponse = new ApiResponse<>();
    //         errorResponse.setStatus("failed");
    //         errorResponse.setMessage(e.getMessage());

    //         return errorResponse;
    //     }
    // }

}
