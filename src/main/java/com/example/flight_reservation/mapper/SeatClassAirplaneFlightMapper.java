package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.SeatClassAirplaneFlightRequest;
import com.example.flight_reservation.dto.response.SeatClassAirplaneFlightResponse;
import com.example.flight_reservation.entity.SeatClassAirplaneFlight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatClassAirplaneFlightMapper {
    SeatClassAirplaneFlight toEntity(SeatClassAirplaneFlightRequest dto);
    @Mapping(target = "seatClassAirplaneId", source = "seatClassAirplane.id")
    @Mapping(target = "seatClassName", source = "seatClassAirplane.seatClass.name")
    @Mapping(target = "flightId", source = "flight.id")
    SeatClassAirplaneFlightResponse toResponse(SeatClassAirplaneFlight entity);
}
