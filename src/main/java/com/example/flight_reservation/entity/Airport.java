package com.example.flight_reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "airports")
@Data
public class Airport {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "airport_id")
  private Long id;

  @Column(name = "airport_name", nullable = false, length = 100)
  private String name;

  @Column(name = "iata_code", length = 3)
  private String iataCode;

  @Column(name = "icao_code", length = 4)
  private String icaoCode;

  @Column(name = "city", length = 100)
  private String city;

  @Column(name = "country", length = 100)
  private String country;

  @Column(name = "address", length = 250)
  private String address;
}
