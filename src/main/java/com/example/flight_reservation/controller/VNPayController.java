//package com.example.flight_reservation.controller;
//
//import com.example.flight_reservation.dto.response.VNPayResponse;
//import com.example.flight_reservation.service.Booking.BookingService;
//import java.util.Map;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/vnpay")
//public class VNPayController {
//
//  private final BookingService bookingService;
//
//  public VNPayController(BookingService bookingService) {
//    this.bookingService = bookingService;
//  }
//
//  @PostMapping
//  public ResponseEntity<VNPayResponse> createPaymentURL(@RequestParam Long bookingId) {
//    VNPayResponse response = bookingService.createPaymentUrl(bookingId);
//    return ResponseEntity.ok(response);
//  }
//
//  @GetMapping("/vnpay-callback")
//  public ResponseEntity<VNPayResponse> vnpayCallback(@RequestParam Map<String, String> params) {
//    VNPayResponse response = bookingService.processPaymentCallback(params);
//    return ResponseEntity.ok(response);
//  }
//}