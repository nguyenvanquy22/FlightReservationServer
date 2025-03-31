package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.FlightRequest;
import com.example.flight_reservation.dto.response.FlightResponse;
import com.example.flight_reservation.entity.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    Flight toEntity(FlightRequest dto);
    FlightResponse toResponse(Flight entity);
}
