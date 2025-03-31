package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.SeatClassAirplaneFlightRequest;
import com.example.flight_reservation.dto.response.SeatClassAirplaneFlightResponse;
import com.example.flight_reservation.entity.SeatClassAirplaneFlight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatClassAirplaneFlightMapper {
    SeatClassAirplaneFlight toEntity(SeatClassAirplaneFlightRequest dto);
    SeatClassAirplaneFlightResponse toResponse(SeatClassAirplaneFlight entity);
}
