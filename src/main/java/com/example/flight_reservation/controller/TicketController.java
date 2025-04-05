package com.example.flight_reservation.controller;

import com.example.flight_reservation.dto.request.TicketRequest;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.dto.response.ApiResponse;
import com.example.flight_reservation.service.Ticket.TicketService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Endpoint tạo mới Ticket
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> createTicket(@RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>(true, "Ticket created successfully", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    // Endpoint cập nhật Ticket
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> updateTicket(@PathVariable Long id,
                                                                    @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.updateTicket(id, request);
        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>(true, "Ticket updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint xóa Ticket
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTicket(@PathVariable Long id) {
        ApiResponse<Void> apiResponse = ticketService.deleteTicket(id);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy thông tin Ticket theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketResponse>> getTicketById(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicketById(id);
        ApiResponse<TicketResponse> apiResponse = new ApiResponse<>(true, "Ticket retrieved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // Endpoint lấy danh sách tất cả Ticket
    @GetMapping
    public ResponseEntity<ApiResponse<List<TicketResponse>>> getAllTickets() {
        List<TicketResponse> responses = ticketService.getAllTickets();
        ApiResponse<List<TicketResponse>> apiResponse = new ApiResponse<>(true, "Tickets retrieved successfully", responses);
        return ResponseEntity.ok(apiResponse);
    }
}
