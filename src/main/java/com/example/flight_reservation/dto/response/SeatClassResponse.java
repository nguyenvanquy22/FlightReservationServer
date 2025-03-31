package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class SeatClassResponse {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
}