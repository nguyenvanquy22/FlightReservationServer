package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.AirlineRequest;
import com.example.flight_reservation.dto.response.AirlineResponse;
import com.example.flight_reservation.entity.Airline;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirlineMapper {
    Airline toEntity(AirlineRequest dto);
    AirlineResponse toResponse(Airline entity);
}