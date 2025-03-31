package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.PassengerRequest;
import com.example.flight_reservation.dto.response.PassengerResponse;
import com.example.flight_reservation.entity.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {
    Passenger toEntity(PassengerRequest dto);
    PassengerResponse toResponse(Passenger entity);
}
