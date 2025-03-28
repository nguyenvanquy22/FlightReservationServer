package com.example.flight_reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airlines")
@Data
public class Airline {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "airline_id")
  private Long id;

  @Column(name = "airline_name", nullable = false, length = 100)
  private String name;

  @Column(name = "iata_code", unique = true, length = 2)
  private String iataCode;

  @Column(name = "icao_code", unique = true, length = 3)
  private String icaoCode;
}
