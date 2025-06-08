package com.example.flight_reservation.dto.request;

import com.example.flight_reservation.entity.enums.UserRole;
import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
    private UserRole role;
}