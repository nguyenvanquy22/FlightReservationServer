package com.example.flight_reservation.dto.response;

import com.example.flight_reservation.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Long userId;
    private UserRole userRole;
}
