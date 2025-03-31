package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class AirlineResponse {
    private Long id;
    private String name;
    private String iataCode;
    private String icaoCode;
}