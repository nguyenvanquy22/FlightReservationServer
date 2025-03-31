package com.example.flight_reservation.dto.response;

import com.example.flight_reservation.entity.enums.AirplaneStatus;
import lombok.Data;

@Data
public class AirplaneResponse {
  private Long id;
  private AirlineResponse airline;
  private String model;
  private String registrationCode;
  private Integer capacity;
  private AirplaneStatus status;
}