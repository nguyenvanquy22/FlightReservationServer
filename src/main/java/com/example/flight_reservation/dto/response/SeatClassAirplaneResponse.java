package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class SeatClassAirplaneResponse {
    private Long id;
    private SeatClassResponse seatClass;
    private AirplaneResponse airplane;
    private Integer rowCount;
    private Integer columnCount;
    private Integer seatQuantity;
}