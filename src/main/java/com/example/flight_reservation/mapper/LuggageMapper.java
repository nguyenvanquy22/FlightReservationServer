package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.LuggageRequest;
import com.example.flight_reservation.dto.response.LuggageResponse;
import com.example.flight_reservation.entity.Luggage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LuggageMapper {
    Luggage toEntity(LuggageRequest dto);
    LuggageResponse toResponse(Luggage entity);
}
