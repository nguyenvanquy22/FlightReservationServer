package com.example.flight_reservation.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.example.flight_reservation.entity.enums.BookingStatus;
import lombok.Data;

@Data
public class BookingRequest {
  private Long userId;
  private Double totalPrice;
  private List<PassengerRequest> passengersRequest;
  private List<FlightBookingRequest> flightBookingRequests;
}
