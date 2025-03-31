package com.example.flight_reservation.dto.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SeatClassAirplaneFlightResponse {
    private Long id;
    private SeatClassAirplaneResponse seatClassAirplane;
    private Long flightId;
    private BigDecimal seatPrice;
    private String seatClassName;
    private Integer availableSeats;
}