package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class FlightBookingRequest {
    private Long flightId;
    private Long seatOptionId;
}
