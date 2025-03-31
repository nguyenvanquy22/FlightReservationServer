package com.example.flight_reservation.dto.request;

import java.time.LocalDateTime;
import com.example.flight_reservation.entity.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingRequest {
  private Long userId;
  private LocalDateTime bookingDate;
  private BookingStatus status;
}
