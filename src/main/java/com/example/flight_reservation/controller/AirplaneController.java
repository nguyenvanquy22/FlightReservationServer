package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.AirplaneRequest;
import com.example.flight_reservation.dto.response.AirplaneResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Airplane.AirplaneService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airplanes")
public class AirplaneController {

    @Autowired
    private AirplaneService airplaneService;

    // Endpoint tạo máy bay
    @PostMapping
    public ResponseEntity<ApiResponse<AirplaneResponse>> createAirplane(@RequestBody AirplaneRequest request) {
        AirplaneResponse response = airplaneService.createAirplane(request);
        ApiResponse<AirplaneResponse> apiResponse = new ApiResponse<>(true, "Airplane created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Endpoint cập nhật máy bay
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirplaneResponse>> updateAirplane(@PathVariable Long id,
                                                                        @RequestBody AirplaneRequest request) {
        AirplaneResponse response = airplaneService.updateAirplane(id, request);
        ApiResponse<AirplaneResponse> apiResponse = new ApiResponse<>(true, "Airplane updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint xóa máy bay
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirplane(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = airplaneService.deleteAirplane(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy thông tin máy bay theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirplaneResponse>> getAirplaneById(@PathVariable Long id) {
        AirplaneResponse response = airplaneService.getAirplaneById(id);
        ApiResponse<AirplaneResponse> apiResponse = new ApiResponse<>(true, "Airplane retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy danh sách máy bay
    @GetMapping
    public ResponseEntity<ApiResponse<List<AirplaneResponse>>> getAllAirplanes() {
        List<AirplaneResponse> responses = airplaneService.getAllAirplanes();
        ApiResponse<List<AirplaneResponse>> apiResponse = new ApiResponse<>(true, "Airplanes retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint tìm kiếm máy bay theo model
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AirplaneResponse>>> searchAirplanes(@RequestParam("keyword") String keyword) {
        List<AirplaneResponse> responses = airplaneService.searchAirplanes(keyword);
        ApiResponse<List<AirplaneResponse>> apiResponse = new ApiResponse<>(true, "Airplanes search result", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
