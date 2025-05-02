package com.example.flight_reservation.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransitResponse {
    private Long id;
    private Long flightId;
    private Long airportId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer transitOrder;
}
