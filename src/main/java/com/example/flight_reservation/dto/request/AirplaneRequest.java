package com.example.flight_reservation.dto.request;

import com.example.flight_reservation.entity.enums.AirplaneStatus;
import lombok.Data;

@Data
public class AirplaneRequest {
  private Long airlineId;
  private String model;
  private String registrationCode;
  private Integer capacity;
  private AirplaneStatus status;
}