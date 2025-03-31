package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.SeatClassAirplaneRequest;
import com.example.flight_reservation.dto.response.SeatClassAirplaneResponse;
import com.example.flight_reservation.entity.SeatClassAirplane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatClassAirplaneMapper {
    SeatClassAirplane toEntity(SeatClassAirplaneRequest dto);
    SeatClassAirplaneResponse toResponse(SeatClassAirplane entity);
}
