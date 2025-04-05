package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.AuthRequest;
import com.example.flight_reservation.dto.response.AuthResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.User.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint đăng nhập
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.login(authRequest);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(true, "Login successful", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint đăng ký
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody AuthRequest authRequest) {
        AuthResponse response = authService.register(authRequest);
        ApiResponse<AuthResponse> apiResponse = new ApiResponse<>(true, "Registration successful", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }
}
