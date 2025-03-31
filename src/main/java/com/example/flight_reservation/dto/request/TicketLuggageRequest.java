package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class TicketLuggageRequest {
    private Long ticketId;
    private Long LuggageId;
}
