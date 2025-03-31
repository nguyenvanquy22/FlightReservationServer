package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.BookingRequest;
import com.example.flight_reservation.dto.response.BookingResponse;
import com.example.flight_reservation.entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking toEntity(BookingRequest dto);
    BookingResponse toResponse(Booking entity);
}
