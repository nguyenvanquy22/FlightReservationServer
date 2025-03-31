package com.example.flight_reservation.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class TicketResponse {
    private Long id;
    private SeatClassAirplaneFlightResponse seatClassAirplaneFlight;
    private PassengerResponse passenger;
    private Long bookingId;
    private String seatNumber;
    private BigDecimal price;
    private List<LuggageResponse> luggages;
}