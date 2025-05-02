package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.SeatClassRequest;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.dto.response.SeatClassResponse;
import com.example.flight_reservation.service.SeatClass.SeatClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seat-classes")
public class SeatClassController {

    @Autowired
    private SeatClassService seatClassService;

    @PostMapping
    public ResponseEntity<ApiResponse<SeatClassResponse>> create(@RequestBody SeatClassRequest request) {
        SeatClassResponse response = seatClassService.createSeatClass(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Seat class created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SeatClassResponse>> update(@PathVariable Long id,
                                                                 @RequestBody SeatClassRequest request) {
        SeatClassResponse response = seatClassService.updateSeatClass(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Seat class updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        seatClassService.deleteSeatClass(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Seat class deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SeatClassResponse>>> getAll() {
        List<SeatClassResponse> list = seatClassService.getAllSeatClasses();
        return ResponseEntity.ok(new ApiResponse<>(true, "Seat classes fetched successfully", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SeatClassResponse>> getById(@PathVariable Long id) {
        SeatClassResponse response = seatClassService.getSeatClassById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Seat class fetched successfully", response));
    }
}

