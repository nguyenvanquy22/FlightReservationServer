package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class TicketLuggageResponse {
    private Long id;
    private TicketResponse ticket;
    private LuggageResponse luggage;
}
