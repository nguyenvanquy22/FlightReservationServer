package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.TicketRequest;
import com.example.flight_reservation.dto.response.TicketResponse;
import com.example.flight_reservation.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toEntity(TicketRequest dto);
    @Mapping(target = "seatClassAirplaneFlightId", source = "seatClassAirplaneFlight.id")
    TicketResponse toResponse(Ticket entity);
}
