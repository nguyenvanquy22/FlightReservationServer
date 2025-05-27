package com.example.flight_reservation.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.flight_reservation.entity.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingRequest {
  private Long userId;
  private BigDecimal totalPrice;
  private List<PassengerRequest> passengersRequest;
  private List<FlightBookingRequest> flightBookingRequests;
  private BookingStatus status;
}
