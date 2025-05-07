package com.example.flight_reservation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import com.example.flight_reservation.entity.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingResponse {
  private Long id;
  private UserResponse user;
  private LocalDateTime bookingDate;
  private BookingStatus status;
  private List<TicketResponse> tickets;
  private PaymentResponse payment;
}