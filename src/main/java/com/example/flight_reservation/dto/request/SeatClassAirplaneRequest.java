package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class SeatClassAirplaneRequest {
    private Long seatClassId;
    private Long airplaneId;
    private Integer rowCount;
    private Integer columnCount;
    private Integer seatQuantity;
}