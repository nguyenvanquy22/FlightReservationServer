package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.AirportRequest;
import com.example.flight_reservation.dto.response.AirportResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Airport.AirportService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    @Autowired
    private AirportService airportService;

    // Tạo mới Airport
    @PostMapping
    public ResponseEntity<ApiResponse<AirportResponse>> createAirport(@RequestBody AirportRequest request) {
        AirportResponse response = airportService.createAirport(request);
        ApiResponse<AirportResponse> apiResponse = new ApiResponse<>(true, "Airport created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Cập nhật Airport
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> updateAirport(@PathVariable Long id,
                                                                      @RequestBody AirportRequest request) {
        AirportResponse response = airportService.updateAirport(id, request);
        ApiResponse<AirportResponse> apiResponse = new ApiResponse<>(true, "Airport updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Xóa Airport
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirport(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = airportService.deleteAirport(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Lấy thông tin Airport theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirportResponse>> getAirportById(@PathVariable Long id) {
        AirportResponse response = airportService.getAirportById(id);
        ApiResponse<AirportResponse> apiResponse = new ApiResponse<>(true, "Airport retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Lấy danh sách tất cả Airports
    @GetMapping
    public ResponseEntity<ApiResponse<List<AirportResponse>>> getAllAirports() {
        List<AirportResponse> responses = airportService.getAllAirports();
        ApiResponse<List<AirportResponse>> apiResponse = new ApiResponse<>(true, "Airports retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }

    // Tìm kiếm Airports theo tên
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AirportResponse>>> searchAirports(@RequestParam("keyword") String keyword) {
        List<AirportResponse> responses = airportService.searchAirports(keyword);
        ApiResponse<List<AirportResponse>> apiResponse = new ApiResponse<>(true, "Airports search result", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
