package com.example.flight_reservation.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class TicketResponse {
    private Long id;
    private Long seatClassAirplaneFlightId;
    private String seatClassName;
    private PassengerResponse passenger;
    private Long bookingId;
    private String flightNumber;
    private String seatNumber;
    private BigDecimal price;
    private List<LuggageResponse> luggages;
}