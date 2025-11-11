package com.tlu.EmployeeManagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.tlu.EmployeeManagement.dto.request.RegisterUserDto;
import com.tlu.EmployeeManagement.dto.request.UserFilterDto;
import com.tlu.EmployeeManagement.dto.request.UserUpdateDto;
import com.tlu.EmployeeManagement.dto.response.ApiResponse;
import com.tlu.EmployeeManagement.dto.response.PagedResponse;
import com.tlu.EmployeeManagement.dto.response.UserResponse;
import com.tlu.EmployeeManagement.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @GetMapping
    public ApiResponse<PagedResponse<UserResponse>> getUsers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String search
    ) {
        UserFilterDto filterDto = UserFilterDto.builder()
                .page(page)
                .pageSize(pageSize)
                .country(country)
                .search(search)
                .build();

        PagedResponse<UserResponse> users = userService.getUser(filterDto);

        ApiResponse<PagedResponse<UserResponse>> response = new ApiResponse<>();
        response.setStatus("success");
        response.setMessage("Get users successfully");
        response.setData(users);
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Integer id) {
        UserResponse user = userService.getUserById(id);

        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setStatus("success");
        response.setMessage("Get user successfully");
        response.setData(user);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody RegisterUserDto dto) {
        UserResponse user = userService.createUser(dto);

        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setStatus("success");
        response.setMessage("User created successfully");
        response.setData(user);
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Integer id,
                                                @Valid @RequestBody UserUpdateDto dto) {
        UserResponse user = userService.updateUser(id, dto);

        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setStatus("success");
        response.setMessage("User updated successfully");
        response.setData(user);
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);

        ApiResponse<Void> response = new ApiResponse<>();
        response.setStatus("success");
        response.setMessage("User deleted successfully");
        return response;
    }
}
