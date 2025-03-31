package com.example.flight_reservation.dto.request;
import java.time.LocalDateTime;
import com.example.flight_reservation.entity.enums.FlightStatus;
import lombok.Data;

@Data
public class FlightRequest {
    private Long airplaneId;
    private Long originAirportId;
    private Long destinationAirportId;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
}
