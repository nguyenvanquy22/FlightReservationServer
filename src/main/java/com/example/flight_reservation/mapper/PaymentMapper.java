package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.PaymentRequest;
import com.example.flight_reservation.dto.response.PaymentResponse;
import com.example.flight_reservation.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    Payment toEntity(PaymentRequest dto);
    @Mapping(target = "bookingId", source = "booking.id")
    PaymentResponse toResponse(Payment entity);
}
