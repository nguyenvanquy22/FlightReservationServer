package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.TicketRequest;
import com.example.flight_reservation.dto.response.TicketLuggageResponse;
import com.example.flight_reservation.entity.TicketLuggage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketLuggageMapper {
    TicketLuggage toEntity(TicketRequest dto);
    TicketLuggageResponse toResponse(TicketLuggage entity);
}
