package com.example.flight_reservation.mapper;

import com.example.flight_reservation.dto.request.UserRequest;
import com.example.flight_reservation.dto.response.UserResponse;
import com.example.flight_reservation.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequest dto);
    UserResponse toResponse(User entity);
}
