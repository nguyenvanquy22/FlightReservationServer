package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.PassengerRequest;
import com.example.flight_reservation.dto.response.PassengerResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Passenger.PassengerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    // Endpoint tạo mới Passenger
    @PostMapping
    public ResponseEntity<ApiResponse<PassengerResponse>> createPassenger(@RequestBody PassengerRequest request) {
        PassengerResponse response = passengerService.createPassenger(request);
        ApiResponse<PassengerResponse> apiResponse = new ApiResponse<>(true, "Passenger created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Endpoint cập nhật Passenger
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PassengerResponse>> updatePassenger(@PathVariable Long id,
                                                                          @RequestBody PassengerRequest request) {
        PassengerResponse response = passengerService.updatePassenger(id, request);
        ApiResponse<PassengerResponse> apiResponse = new ApiResponse<>(true, "Passenger updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint xóa Passenger
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePassenger(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = passengerService.deletePassenger(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy thông tin Passenger theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PassengerResponse>> getPassengerById(@PathVariable Long id) {
        PassengerResponse response = passengerService.getPassengerById(id);
        ApiResponse<PassengerResponse> apiResponse = new ApiResponse<>(true, "Passenger retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy danh sách tất cả Passenger
    @GetMapping
    public ResponseEntity<ApiResponse<List<PassengerResponse>>> getAllPassengers() {
        List<PassengerResponse> responses = passengerService.getAllPassengers();
        ApiResponse<List<PassengerResponse>> apiResponse = new ApiResponse<>(true, "Passengers retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
