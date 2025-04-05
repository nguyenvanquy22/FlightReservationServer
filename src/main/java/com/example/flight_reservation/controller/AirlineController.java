package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.AirlineRequest;
import com.example.flight_reservation.dto.response.AirlineResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Airline.AirlineService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {

    @Autowired
    private AirlineService airlineService;

    // Create Airline
    @PostMapping
    public ResponseEntity<ApiResponse<AirlineResponse>> createAirline(@RequestBody AirlineRequest request) {
        AirlineResponse response = airlineService.createAirline(request);
        ApiResponse<AirlineResponse> apiResponse = new ApiResponse<>(true, "Airline created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Update Airline
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AirlineResponse>> updateAirline(@PathVariable Long id,
                                                                      @RequestBody AirlineRequest request) {
        AirlineResponse response = airlineService.updateAirline(id, request);
        ApiResponse<AirlineResponse> apiResponse = new ApiResponse<>(true, "Airline updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Delete Airline
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAirline(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = airlineService.deleteAirline(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Get Airline by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AirlineResponse>> getAirlineById(@PathVariable Long id) {
        AirlineResponse response = airlineService.getAirlineById(id);
        ApiResponse<AirlineResponse> apiResponse = new ApiResponse<>(true, "Airline retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Get All Airlines
    @GetMapping
    public ResponseEntity<ApiResponse<List<AirlineResponse>>> getAllAirlines() {
        List<AirlineResponse> responses = airlineService.getAllAirlines();
        ApiResponse<List<AirlineResponse>> apiResponse = new ApiResponse<>(true, "Airlines retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }

    // Search Airlines by keyword
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<AirlineResponse>>> searchAirlines(@RequestParam("keyword") String keyword) {
        List<AirlineResponse> responses = airlineService.searchAirlines(keyword);
        ApiResponse<List<AirlineResponse>> apiResponse = new ApiResponse<>(true, "Airlines search result", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
