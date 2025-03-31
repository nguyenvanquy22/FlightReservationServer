package com.example.flight_reservation.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SeatClassAirplaneFlightRequest {
    private Long seatClassAirplaneId;
    private Long flightId;
    private BigDecimal seatPrice;
}