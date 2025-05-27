package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.BookingRequest;
import com.example.flight_reservation.dto.response.BookingResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Booking.BookingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Endpoint tạo mới Booking
    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(@RequestBody BookingRequest request) {
        BookingResponse response = bookingService.createBooking(request);
        ApiResponse<BookingResponse> apiResponse = new ApiResponse<>(true, "Booking created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Endpoint cập nhật Booking
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> updateBooking(@PathVariable Long id,
                                                                      @RequestBody BookingRequest request) {
        BookingResponse response = bookingService.updateBooking(id, request);
        ApiResponse<BookingResponse> apiResponse = new ApiResponse<>(true, "Booking updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint xóa Booking
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBooking(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = bookingService.deleteBooking(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy thông tin Booking theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBookingById(@PathVariable Long id) {
        BookingResponse response = bookingService.getBookingById(id);
        ApiResponse<BookingResponse> apiResponse = new ApiResponse<>(true, "Booking retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy danh sách tất cả Bookings
    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAllBookings() {
        List<BookingResponse> responses = bookingService.getAllBookings();
        ApiResponse<List<BookingResponse>> apiResponse = new ApiResponse<>(true, "Bookings retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getByUserId(
            @PathVariable Long userId) {

        List<BookingResponse> list = bookingService.getBookingsByUserId(userId);
        ApiResponse<List<BookingResponse>> resp =
                new ApiResponse<>(true, "Bookings for user retrieved successfully", list);
        return ResponseEntity.ok(resp);
    }
}
