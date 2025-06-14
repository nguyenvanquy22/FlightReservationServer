package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.TransitRequest;
import com.example.flight_reservation.dto.response.TransitResponse;
import com.example.flight_reservation.entity.Transit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransitMapper {
    Transit toEntity(TransitRequest dto);
    @Mapping(target = "flightId", source = "flight.id")
    @Mapping(target = "airportId", source = "transitAirport.id")
    TransitResponse toResponse(Transit entity);
}
