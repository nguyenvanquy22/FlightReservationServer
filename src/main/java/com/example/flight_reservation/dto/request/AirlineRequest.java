package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class AirlineRequest {
    private String name;
    private String iataCode;
    private String icaoCode;
}