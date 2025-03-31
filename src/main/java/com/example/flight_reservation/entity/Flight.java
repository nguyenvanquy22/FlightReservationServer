package com.example.flight_reservation.entity;

import java.time.LocalDateTime;

import com.example.flight_reservation.entity.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "flights")
@Data
public class Flight {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "flight_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "airplane_id", nullable = false)
  private Airplane airplane;

  @ManyToOne
  @JoinColumn(name = "origin_airport_id", nullable = false)
  private Airport originAirport;

  @ManyToOne
  @JoinColumn(name = "destination_airport_id", nullable = false)
  private Airport destinationAirport;

  @Column(name = "flight_number", unique = true, length = 10)
  private String flightNumber;

  @Column(name = "departure_time")
  private LocalDateTime departureTime;

  @Column(name = "arrival_time")
  private LocalDateTime arrivalTime;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private FlightStatus status = FlightStatus.SCHEDULED;
}
