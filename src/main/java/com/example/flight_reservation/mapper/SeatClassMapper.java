package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.SeatClassRequest;
import com.example.flight_reservation.dto.response.SeatClassResponse;
import com.example.flight_reservation.entity.SeatClass;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatClassMapper {
    SeatClass toEntity(SeatClassRequest dto);
    SeatClassResponse toResponse(SeatClass entity);
}
