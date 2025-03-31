package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.AirplaneRequest;
import com.example.flight_reservation.dto.response.AirplaneResponse;
import com.example.flight_reservation.entity.Airplane;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AirplaneMapper {
    Airplane toEntity(AirplaneRequest dto);
    AirplaneResponse toResponse(Airplane entity);
}
