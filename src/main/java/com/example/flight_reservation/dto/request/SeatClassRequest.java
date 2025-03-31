package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class SeatClassRequest {
    private String name;
    private String description;
    private Integer displayOrder;
}