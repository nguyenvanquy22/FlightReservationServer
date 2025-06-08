package com.example.flight_reservation.dto.request;

import com.example.flight_reservation.entity.enums.UserRole;
import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private UserRole role;
}
