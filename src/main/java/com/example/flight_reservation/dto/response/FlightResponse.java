package com.example.flight_reservation.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import com.example.flight_reservation.entity.enums.FlightStatus;
import lombok.Data;

@Data
public class FlightResponse {
    private Long id;
    private AirplaneResponse airplane;
    private AirportResponse originAirport;
    private AirportResponse destinationAirport;
    private String flightNumber;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus status;
    private List<TransitResponse> transits;
    private List<SeatClassAirplaneFlightResponse> seatOptions;
}
