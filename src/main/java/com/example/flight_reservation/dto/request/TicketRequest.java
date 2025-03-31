package com.example.flight_reservation.dto.request;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TicketRequest {
    private Long seatClassAirplaneFlightId;
    private Long passengerId;
    private Long bookingId;
    private String seatNumber;
    private BigDecimal price;
}