package com.example.flight_reservation.dto.request;

import lombok.Data;

@Data
public class AirportRequest {
  private String name;
  private String iataCode;
  private String icaoCode;
  private String city;
  private String country;
  private String address;
}