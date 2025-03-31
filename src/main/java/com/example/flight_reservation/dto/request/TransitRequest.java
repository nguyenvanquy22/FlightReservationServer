package com.example.flight_reservation.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransitRequest {
    private Long flightId;
    private Long airportId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer transitOrder;
}
