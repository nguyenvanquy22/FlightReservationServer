package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.UserRequest;
import com.example.flight_reservation.dto.response.UserResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.User.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Tạo mới User
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserRequest request) {
        UserResponse response = userService.createUser(request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(true, "User created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật User
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id,
                                                                @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(true, "User updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Xóa User
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = userService.deleteUser(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Lấy thông tin User theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>(true, "User retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Lấy danh sách User
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> responses = userService.getAllUsers();
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>(true, "Users retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
