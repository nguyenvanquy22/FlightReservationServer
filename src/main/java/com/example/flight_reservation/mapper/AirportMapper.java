package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.AirportRequest;
import com.example.flight_reservation.dto.response.AirportResponse;
import com.example.flight_reservation.entity.Airport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    Airport toEntity(AirportRequest dto);
    AirportResponse toResponse(Airport entity);
}
