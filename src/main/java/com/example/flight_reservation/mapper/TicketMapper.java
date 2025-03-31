package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.TicketRequest;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketRequest dto);
    TicketResponse toResponse(Ticket entity);
}
