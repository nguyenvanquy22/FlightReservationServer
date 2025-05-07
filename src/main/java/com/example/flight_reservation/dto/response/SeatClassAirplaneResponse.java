package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class SeatClassAirplaneResponse {
    private Long id;
    private Long seatClassId;
    private String seatClassName;
    private Long airplaneId;
    private Integer rowCount;
    private Integer columnCount;
}