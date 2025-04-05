package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.FlightRequest;
import com.example.flight_reservation.dto.response.FlightResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Flight.FlightService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    // Endpoint tạo mới Flight
    @PostMapping
    public ResponseEntity<ApiResponse<FlightResponse>> createFlight(@RequestBody FlightRequest request) {
        FlightResponse response = flightService.createFlight(request);
        ApiResponse<FlightResponse> apiResponse = new ApiResponse<>(true, "Flight created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Endpoint cập nhật Flight
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> updateFlight(@PathVariable Long id,
                                                                    @RequestBody FlightRequest request) {
        FlightResponse response = flightService.updateFlight(id, request);
        ApiResponse<FlightResponse> apiResponse = new ApiResponse<>(true, "Flight updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint xóa Flight
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFlight(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = flightService.deleteFlight(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy thông tin Flight theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlightResponse>> getFlightById(@PathVariable Long id) {
        FlightResponse response = flightService.getFlightById(id);
        ApiResponse<FlightResponse> apiResponse = new ApiResponse<>(true, "Flight retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy danh sách tất cả Flights
    @GetMapping
    public ResponseEntity<ApiResponse<List<FlightResponse>>> getAllFlights() {
        List<FlightResponse> responses = flightService.getAllFlights();
        ApiResponse<List<FlightResponse>> apiResponse = new ApiResponse<>(true, "Flights retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint tìm kiếm Flights theo flightNumber
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<FlightResponse>>> searchFlights(@RequestParam("keyword") String keyword) {
        List<FlightResponse> responses = flightService.searchFlights(keyword);
        ApiResponse<List<FlightResponse>> apiResponse = new ApiResponse<>(true, "Flights search result", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
