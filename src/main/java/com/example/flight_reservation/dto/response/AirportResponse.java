package com.example.flight_reservation.dto.response;

import lombok.Data;

@Data
public class AirportResponse {
  private Long id;
  private String name;
  private String iataCode;
  private String icaoCode;
  private String city;
  private String country;
  private String address;
}